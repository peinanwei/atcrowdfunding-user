package com.atguigu.atcrowdfunding.user.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.atguigu.atcrowdfunding.common.bean.AJAXResult;
import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.CertImg;
import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.Ticket;
import com.atguigu.atcrowdfunding.listener.AuthPassListener;
import com.atguigu.atcrowdfunding.listener.AuthRefuseListener;
import com.atguigu.atcrowdfunding.user.service.MemberService;
import com.atguigu.atcrowdfunding.util.ActUtil;
import com.atguigu.atcrowdfunding.util.Const;

@Controller
@RequestMapping(value="member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(value="member")
	public String member(){
		
		return "member";
	}
	@RequestMapping(value="accttype")
	public String accttype(){
		return "member/accttype";
	}
	
	/**
	 * 控制流程的跳转,每次页面跳转都会经过
	 * @param session
	 * @param realname
	 * @param model
	 * @return
	 */
	@RequestMapping(value="apply")
	public String apply(HttpSession session,String realname,Model model){
		
		//拿到登陆的会员信息
		Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
		Ticket ticket =  memberService.queryTicketByMember(loginMember);
		if(ticket == null){
			ticket = new Ticket();
			ticket.setMemberid(loginMember.getId());
			ticket.setStatus("0");
			memberService.insertTicket(ticket);
		}else{
			if("accttype".equals(ticket.getPstep())){
				return "member/basicinfo";
			}else if("basicinfo".equals(ticket.getPstep())){
				//跳转页面的同时查询出
				List<Cert> certs= memberService.queryCertByType(loginMember.getAccttype());
				model.addAttribute("certs", certs);
				return "member/uploadfile";
			}else if("uploadCertFile".equals(ticket.getPstep())){
				return "member/sendEmail";
			}else if("sendEmail".equals(ticket.getPstep())){
				return "member/checkemail";
			}
		}
		return "member/accttype";
	}
	
	/**
	 * 验证验证码
	 * @param session
	 * @param authcode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkemail")
	public Object checkemail(HttpSession session, String authcode){
		AJAXResult result = new AJAXResult();
		
		try {
			//根据用户id查询流程实例的piid,根据piid查询验证码
			Member loginMember =  (Member) session.getAttribute(Const.LOGIN_MEMBER);
			Ticket ticket = memberService.queryTicketByMember(loginMember);
			
			//验证审批单中的验证码,
			if(ticket.getAuthcode().equals(authcode)){
				//设置会员的状态
				loginMember.setAuthstatus("1");
				memberService.updateAuthstatus(loginMember);
				
				
				//完成流程执行下一步
				TaskQuery query = taskService.createTaskQuery();
				Task task = query.processInstanceId(ticket.getPiid())
					.taskAssignee(loginMember.getLoginacct())
					.singleResult();
						
				taskService.complete(task.getId());
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 开启流程执行第一步,发送邮件
	 * @param session
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="sendEmail")
	public Object startProcess(HttpSession session,String email ){
		AJAXResult result = new AJAXResult();
		
		try {
			
			Member loginMember =  (Member) session.getAttribute(Const.LOGIN_MEMBER);
			//如果输入的邮箱不正确,则发送失败
			if(!email.equals(loginMember.getEmail())){
				result.setSuccess(false);
				return result;
			}
			
			//将流程中的变量进行赋值,放到map集合中,流程开始时入参
			Map<String,Object> varMap = new HashMap<String,Object>();
			//创建四位验证码
			StringBuilder authcode = new StringBuilder();
			for(int i =0;i < 4 ;i++){
				authcode.append(new Random().nextInt(10));
			}
		
			//验证码
			varMap.put("authcode", authcode);
			//邮箱
			varMap.put("email", email);
			//会员账号
			varMap.put("loginacct", loginMember.getLoginacct());
			//审核通过监听器
			varMap.put("passListener", new AuthPassListener());
			varMap.put("passListener", new AuthRefuseListener());
			
			//获取当前会员的当前审批流程
			
				//获得实名认证审批实例的查询
			ProcessDefinitionQuery query = 
					repositoryService.createProcessDefinitionQuery();
				//查询到流程实例
			ProcessDefinition pd = 
					query.processDefinitionKey("auth").latestVersion().singleResult();
				//启动流程实例
			ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId(), varMap);
			
			//更新流程审批步骤,同时将验证码,和流程实例id传入
			
			Ticket ticket = memberService.queryTicketByMember(loginMember);
			ticket.setPstep("sendEmail");
			ticket.setPiid(pi.getId());
			//验证码
			ticket.setAuthcode(authcode.toString());
			
			memberService.updateTicket(ticket);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 上传资质文件
	 * @param session
	 * @param ds
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value="uploadCertFile")
	public Object uploadCertFile(HttpSession session,Datas ds ){
		AJAXResult result = new AJAXResult();
		
		try {
			//获取application,为了得到需要保存文件的真实地址
			ServletContext application = session.getServletContext();
			String realPath = application.getRealPath("pics");
			
			List<CertImg> imgs = ds.getCertImgs();
			Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
			//还要更新数据库的几个表
			//遍历集合拿到资质文件
			for (CertImg certImg : imgs) {
				//
				MultipartFile file =  certImg.getCertImg();
				//获取文件的名字
				String filename = file.getOriginalFilename();
				//创建文件的真实名字
				String realname = UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."));
				
				//设置文件的保存地址	
				String filepath = realPath+"/cert/" +realname;
				
				//将文件复制
				file.transferTo(new File(realname));
				
				//会员id保存
				certImg.setMemberid(loginMember.getId());
				//将文件保存到certimg中?
				certImg.setIconpath(realname);
				
			}
			
			//更新数据库
			memberService.insertMemberCert(ds.getCertImgs());
			//更新流程审批对象
			Ticket ticket = memberService.queryTicketByMember(loginMember);
			ticket.setPstep("uploadCertFile");
			//更新数据库
			memberService.updateTicketProcessStep(ticket);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/**
	 * 更新会员的真实名字,身份证号以及电话
	 * @param session
	 * @param member
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="updateBasicinfo")
	public Object updateBasicinfo(HttpSession session, Member member){
		AJAXResult result = new AJAXResult();
		try {
			//拿到登陆的会员信息
			Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
			//此时只是更新了session中的member信息,
			loginMember.setRealname(member.getRealname());
			loginMember.setCardnum(member.getCardnum());
			loginMember.setTel(member.getTel());
			
			//更新数据库
			memberService.updateMemberBasicinfo(loginMember);
			
			//同时更新流程审批单
			memberService.udpateAccttype(loginMember);
			
			// 更新当前的流程步骤,因为流程先经过apply,所以,ticket一定不为空,此时更新流程步骤即可
			Ticket ticket = memberService.queryTicketByMember(loginMember);
			ticket.setPstep("basicinfo");
			memberService.updateTicketProcessStep(ticket);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 更新会员的账户类型
	 * @param session
	 * @param accttype
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="updateAcctype")
	public Object updateAccttype(HttpSession session, String accttype){
		AJAXResult result = new AJAXResult();
		try {
			//拿到登陆的会员信息
			Member loginMember = (Member) session.getAttribute(Const.LOGIN_MEMBER);
			//此时只是更新了session中的member信息,
			loginMember.setAccttype(accttype);
			memberService.udpateAccttype(loginMember);
			//同时更新流程审批单
			
			// 更新当前的流程步骤,因为流程先经过apply,所以,ticket一定不为空,此时更新流程步骤即可
			Ticket ticket = memberService.queryTicketByMember(loginMember);
			
			ticket.setPstep("accttype");
			
			memberService.updateTicketProcessStep(ticket);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
}
