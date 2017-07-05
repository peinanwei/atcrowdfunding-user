package com.atguigu.atcrowdfunding.user.dao;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.common.bean.Cert;
import com.atguigu.atcrowdfunding.common.bean.CertImg;
import com.atguigu.atcrowdfunding.common.bean.Member;
import com.atguigu.atcrowdfunding.common.bean.Ticket;

public interface MemberDao {

	Member queryMember(Map<String, Object> map);

	void udpateAccttype(Member loginMember);

	Ticket queryTicketByMember(Member loginMember);

	void insertTicket(Ticket ticket);

	void updateTicketProcessStep(Ticket ticket);

	List<Cert> queryCertByType(String accttype);

	void insertMemberCert(CertImg certImg);

	void updateTicket(Ticket ticket);

	void updateAuthstatus(Member loginMember);

	void updateMemberBasicinfo(Member loginMember);

}
