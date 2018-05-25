package tech.harish.apps.n26;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.harish.apps.n26.controller.HomeController;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class N26ApplicationTests {

	@Autowired
	private HomeController controller;

	@Test
	public void contexLoads(){
		assertNotNull(controller);
	}

}
