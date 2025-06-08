package org.zerock.apiserver.service;


import org.zerock.apiserver.domain.Member;
import org.zerock.apiserver.dto.MemberDTO;

import java.util.stream.Collectors;

public interface MemberService {

    MemberDTO getKaKaoMember(String accessToken);

    default MemberDTO entityToDTO(Member member) {

        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList()
                        .stream()
                        .map(memberRole -> memberRole.name())
                        .collect(Collectors.toList())
        );

        return dto;
    }
}