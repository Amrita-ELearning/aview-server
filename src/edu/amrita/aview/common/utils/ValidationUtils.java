package edu.amrita.aview.common.utils;


public class ValidationUtils 
{
	public static String integerOnly(String in) 
	{
		String intError = null;
		if (in.matches("^[0-9]+$")) {

		} else {

			intError = "Accept Only Integers";
		}

		return intError;

	}
	public static String AllowedCharForclass(String Cname)  
	{
		String AllowedCharError = null;
		if (Cname.matches("^[a-zA-Z0-9-&.,';:_()/+| ]+$"))
		{
			
		}else
		{
			AllowedCharError = "Special character other than -&.,';:_()/+| are not allowed";
		}
		return AllowedCharError;
	}
	
	public static String AllowedCharForname(String name)  {
		String AllowedCharError = null;
			if (name.matches("^[a-zA-Z0-9-&,.';:_()/+| ]+$")) 
			{

			}
			else 
			{
			AllowedCharError = "Special character other than -&,.';:_()/+| are not allowed";
			}
		
		return AllowedCharError;
	}

	public static String AllowedCharForClassDescrption(String Cdesc) {
		String ClassDescError = null;
		if (Cdesc.matches("^[A-z0-9<>'/@?&*()#$%{}!~=.,;?: ]+$")) {

		} else {
			ClassDescError = "Special character other than <>'/@?&*()#$%{}!~=.,;?:  are not allowed";

		}
		return ClassDescError;

	}

	public static String validateFnameAndLname(String flname) {
		String nameError = null;
		if (flname.matches("^[a-zA-Z0-9-_. ]+$")) {
			if (flname.length() <= 100) {

			} else {
				nameError = "Length exceed the limit";
			}
		} else {
			nameError = "Special charcters other than '_.- ' are not allowed";
		}

		return nameError;

	}

	public static String validateCity(String s) {
		String cityError = null;

		if (s.matches("^[-a-zA-Z0-9_ ]+$")) {

			if (s.length() <= 100) {

			} else {
				cityError = "Length exceeds";
			}

		} else {
			cityError = "Special charcters other than '_- ' are not allowed";

		}
		return cityError;
	}

	public static String validateAddress(String add) {
		String AddError = null;
		if (add.matches("^[a-zA-Z0-9,-/. ]+$")) {
			if (add.length() <= 500) {

			} else {
				AddError = "Length exceeds the limit";
			}

		} else {
			AddError = "Special character other than '-,./ ' are not allowed";

		}
		return AddError;
	}

	public static String AlphanumericOnly(String str) {
		String AlphaError = null;
		if (str.matches("^[a-zA-Z0-9]*$")) {

		} else {
			AlphaError = "Accept Only AlphaNumerics";
		}
		return AlphaError;
	}

	public static String EmailValidation(String email) {
		String emailError = null;

		if (email
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			if (email.length() <= 100) {

			} else {
				emailError = "Length exceeds in the email-id";
			}

		} else {
			emailError = "Email id doesn't start with special character";
		}
		return emailError;

	}

	public static String MobNumValidation(String mob) {
		String mobError = null;
		if (mob.matches("^[1-9]\\d{9}$")) {
		} else {
			mobError = "Enter valid mobile number";
		}
		return mobError;

	}

	
	public static String ZipcodeValidation(String zip) {
		String zipError = null;
		if (zip.matches("^[0-9]{6}")) {

		} else {
			zipError = "Enter valid ZipCode";
		}
		return zipError;
	}

	public static String Timevalidator(String tm) {
		String timeError = null;
		if (tm.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]+$")) {
		} else {
			timeError = "is not in 24hr format";
		}

		return timeError;
	}

	public static String DateValidation(String date) {
		String dateError = null;
		String year=date.substring(0,4);
		int yearCheck=Integer.parseInt(year);
		if(yearCheck<1890)
		{
			dateError = "Enter valid year";
			return dateError;
		}
		else
		{
		String mat="([0-9]{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
	
		
		if (date.matches(mat)) {

		} else {
			dateError = "Enter valid Date";
		}
		}
		return dateError;
	}
	
	public static String AllowedCharForUserAndpass(String Unameandpass)
	{
        String CharForUserAndpass = null;
      //  String systemparam = "a-zA-Z0-9-&,.';:_()/+|";
        if (Unameandpass.matches("^[a-zA-Z0-9-&,.';:_()/+|]+$"))
        {

        } else 
        {
            CharForUserAndpass = "Special characters other than -&,.';:_()/+| are not allowed";
        }
        return CharForUserAndpass;
	}
}