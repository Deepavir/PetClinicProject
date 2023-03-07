package org.springframework.samples.petclinic.service;

import java.nio.file.Paths;
import java.util.Date;

public class ConstantProperty {

	public final static String FOLDERPATH = Paths.get("src/main/resources/static/picture").toAbsolutePath().toString();

	public final static String PETFOLDERPATH = Paths.get("src/main/resources/static/petimages").toAbsolutePath()
			.toString();

	public static String getMailBodyFirstPart(String name, Date vaccinedate, String petname, String ownername) {
		final StringBuilder sb = new StringBuilder();

		sb.append("<html>")
         .append("<body>").append("<h2> Dear ").append(ownername).append(",</h2>")
				.append("<p> This is a friendly reminder about your upcoming appointment with us on ")
				.append(vaccinedate).append(" of your pet ").append(petname).append("<br>")
				.append("Thank you for choosing our services, and we look forward to seeing you.").append("</p>")
				.append("<p>Best Regards").append("<br>").append("Petclinic").append("</p>")
				.append("website link: <a href='https://vetic.in/clinics/gurgaon'>https://vetic.in/clinics/gurgaon</a>")

				.append("</body>").append("</html>");

		return sb.toString();
	}

}