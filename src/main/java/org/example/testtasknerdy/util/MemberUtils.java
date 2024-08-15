package org.example.testtasknerdy.util;

import lombok.experimental.UtilityClass;
import org.example.testtasknerdy.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MemberUtils {
    public static final String NAME = "Name";

    public List<Member> generate(int size) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Member book = new Member(i + 1,
                    String.format(NAME.concat(" %d"), (i + 1)),
                    LocalDateTime.now(),
                    new ArrayList<>());
            members.add(book);
        }
        return members;
    }
}
