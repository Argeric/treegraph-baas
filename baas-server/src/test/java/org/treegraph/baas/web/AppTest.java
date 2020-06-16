package org.treegraph.baas.web;

import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
	static boolean checkPassword(String password) {
		if (password == null || password.length() < 8)
			throw new RuntimeException("password length too short");
		if (password.matches("^[A-Za-z0-9*_^&]{8,32}$")) {
			System.out.println("ssss");
			Pattern numPattern = Pattern.compile("[0-9]+");
			System.out.println("ssss1");
			Pattern lowerPattern = Pattern.compile("[a-z]+");
			System.out.println("ssss2");
			Pattern upperPattern = Pattern.compile("[A-Z]+");
			System.out.println("ssss3");
			Pattern charPattern = Pattern.compile("[*_^&]+");
			System.out.println("ssss4");
			if (numPattern.matcher(password).find() && lowerPattern.matcher(password).find()
					&& upperPattern.matcher(password).find() && charPattern.matcher(password).find())
				return true;
		}
		throw new RuntimeException("must contains number,a-z,A-Z and *_^&");
	}
    
    public static void main(String[] args) {
    	String password = "12abAB*_";
    	boolean flag = checkPassword(password);
    	System.out.println(flag);
	}
}
