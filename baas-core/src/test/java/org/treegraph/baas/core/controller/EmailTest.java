package org.treegraph.baas.core.controller;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.treegraph.baas.core.service.EmailService;


@RunWith(SpringRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest
public class EmailTest {

	@Resource
	private EmailService emailService;
	
	@Test
	public void testEmail(){
		emailService.sendEmail("aaa", "bbb");
	}
}
