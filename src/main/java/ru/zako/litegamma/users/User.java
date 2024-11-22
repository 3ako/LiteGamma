package ru.zako.litegamma.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class User {

   private final String name;
   @Setter private int level;
   @Setter private boolean enable;

}
