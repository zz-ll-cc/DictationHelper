package com.dictation.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName UserSignIn
 * @Description
 * @Author zlc
 * @Date 2020-04-24 11:44
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class UserSignIn {

    private User user;
    private List<Boolean> weekRecord;
    private Long continuousSignIn;

}
