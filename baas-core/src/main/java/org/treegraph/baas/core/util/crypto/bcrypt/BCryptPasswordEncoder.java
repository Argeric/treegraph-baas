/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.treegraph.baas.core.util.crypto.bcrypt;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of PasswordEncoder that uses the BCrypt strong hashing function. Clients can
 * optionally supply a "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The
 * larger the strength parameter the more work will have to be done (exponentially) to hash the
 * passwords. The default value is 10.
 *
 * @author Dave Syer
 */
@Slf4j
public class BCryptPasswordEncoder {

  private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

  private final int strength;

  private final SecureRandom random;

  public BCryptPasswordEncoder() {
    this(-1);
  }

  /**
   * @param strength the log rounds to use, between 4 and 31
   */
  public BCryptPasswordEncoder(int strength) {
    this(strength, null);
  }

  public static BCryptPasswordEncoder create() {
    return new BCryptPasswordEncoder();
  }

  /**
   * @param strength the log rounds to use, between 4 and 31
   * @param random   the secure random instance to use
   */
  public BCryptPasswordEncoder(int strength, SecureRandom random) {
    if (strength != -1 && (strength < BCrypt.MIN_LOG_ROUNDS || strength > BCrypt.MAX_LOG_ROUNDS)) {
      throw new IllegalArgumentException("Bad strength");
    }
    this.strength = strength;
    this.random = random;
  }

  public String encode(CharSequence rawPassword) {
    String salt;
    if (strength > 0) {
      if (random != null) {
        salt = BCrypt.gensalt(strength, random);
      } else {
        salt = BCrypt.gensalt(strength);
      }
    } else {
      salt = BCrypt.gensalt();
    }
    return BCrypt.hashpw(rawPassword.toString(), salt);
  }

  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (encodedPassword == null || encodedPassword.length() == 0) {
      log.error("Empty encoded password");
      return false;
    }

    if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
      log.error("Encoded password does not look like BCrypt");
      return false;
    }

    return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
  }

  public static void main(String[] args) {

    String pwd = "888888ssss";

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String endPwd = encoder.encode(pwd);
    System.out.println(endPwd);

    boolean success1 = encoder.matches(pwd, endPwd);

    System.out.println("pwd=" + pwd + ",endPwd=" + endPwd.length() + ",success1=" + success1);

    boolean success2 = encoder.matches(pwd + "d", endPwd);

    System.out.println("pwd+d=" + pwd + "d,endPwd=" + endPwd + ",success2=" + success2);
  }
}
