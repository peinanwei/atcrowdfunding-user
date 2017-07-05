package com.atguigu.atcrowdfunding.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.CertImg;
import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.Ticket;
import com.atguigu.atcrowdfunding.user.dao.MemberDao;
import com.atguigu.atcrowdfunding.user.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	public Member queryMember(Map<String, Object> map) {
		return memberDao.queryMember(map);
	}

	public void udpateAccttype(Member loginMember) {
		memberDao.udpateAccttype(loginMember);
	}

	public Ticket queryTicketByMember(Member loginMember) {
		return memberDao.queryTicketByMember(loginMember);
	}

	public void insertTicket(Ticket ticket) {
		memberDao.insertTicket(ticket);
	}

	public void updateTicketProcessStep(Ticket ticket) {
		memberDao.updateTicketProcessStep(ticket);
	}

	public List<Cert> queryCertByType(String accttype) {
		return memberDao.queryCertByType(accttype);
	}

	public void insertMemberCert(List<CertImg> list) {
		for (CertImg certImg : list) {
			memberDao.insertMemberCert(certImg);
		}
	}

	public void updateTicket(Ticket ticket) {
		memberDao.updateTicket(ticket);
	}

	public void updateAuthstatus(Member loginMember) {
		memberDao.updateAuthstatus(loginMember);
	}

	public void updateMemberBasicinfo(Member loginMember) {
		memberDao.updateMemberBasicinfo(loginMember);
	}
}
