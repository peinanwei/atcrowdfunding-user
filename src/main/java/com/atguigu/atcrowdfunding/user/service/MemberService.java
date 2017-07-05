package com.atguigu.atcrowdfunding.user.service;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.CertImg;
import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

public interface MemberService {

	/**
	 * 会员登陆验证
	 * @param map
	 * @return
	 */
	Member queryMember(Map<String, Object> map);

	/**
	 * 更新会员的类型(个人,商户?政府?)
	 * @param loginMember
	 */
	void udpateAccttype(Member loginMember);

	/**
	 * 根据会员查询相应的审批单???(会存在多个)
	 * @param loginMember
	 * @return
	 */
	Ticket queryTicketByMember(Member loginMember);

	/**
	 * 创建流程审批单
	 * @param ticket
	 */
	void insertTicket(Ticket ticket);

	/**
	 * 更新流程审批步骤
	 * @param ticket
	 */
	void updateTicketProcessStep(Ticket ticket);

	/**
	 * 根据会员类型查询资质
	 * @param accttype
	 * @return
	 */
	List<Cert> queryCertByType(String accttype);

	/**
	 * 将会员的资质文件传入
	 * @param list
	 */
	void insertMemberCert(List<CertImg> list);
	/**
	 * 更新会员的基本信息
	 * @param loginMember
	 */
	void updateMemberBasicinfo(Member loginMember);
	/**
	 * 开始流程后更新审批
	 * @param ticket
	 */
	void updateTicket(Ticket ticket);

	/**
	 * 验证成功修改会员实名认证状态
	 * @param loginMember
	 */
	void updateAuthstatus(Member loginMember);
	

}
