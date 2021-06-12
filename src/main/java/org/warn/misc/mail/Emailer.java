package org.warn.misc.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Simple mail client using the {@link javax.mail} API. The Emailer object should be instantiated by 
 * providing mail server configuration properties. Eg: <br><br>
 * 
 * <b>mail.smtp.host</b>=mail.mymailhost.org<br>
 * <b>mail.smtp.port</b>=25<br>
 * <b>mail.transport.protocol</b>=smtp<br>
 * <b>mail.smtp.auth</b>=true<br>
 * <b>mail.smtp.user</b>=myuser<br>
 * <b>password</b>=mypassword<br>
 */
public final class Emailer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Emailer.class);
	
	private Properties mailServerConfig = new Properties();
	
	public Emailer(Properties mailServerConfig ) {
		this.mailServerConfig.putAll( mailServerConfig );
	}
	
	public Emailer(String propertyFilePath ) {
		loadConfig( propertyFilePath );
	}
	
	/**
	 * Sends email to provided email addresses.
	 * 
	 * @param fromAddress
	 * @param toAddresses
	 * @param subjectLine
	 * @param body
	 */
	public void sendEmail( String fromAddress, String toAddresses, String subjectLine, String body ) {
		
		// TODO input validation
		
		// No Authenticator argument is used. Authenticators are used to prompt the user for user name and password.
		Session session = Session.getDefaultInstance( mailServerConfig, null );
		MimeMessage message = new MimeMessage(session);
		try {
			/*
			String [] arrToAddresses = toAddresses.split(",");
			InternetAddress [] toAddressesArr = new InternetAddress[arrToAddresses.length];
			for( int i=0; i<arrToAddresses.length; i++ ) {
				toAddressesArr[i] = new InternetAddress(arrToAddresses[i]);
			}
			message.setFrom( new InternetAddress(toAddressesArr) );
			*/
			message.setFrom( new InternetAddress(fromAddress) );
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
			message.addRecipients(Message.RecipientType.TO, toAddresses);
			//message.addRecipient(Message.RecipientType.CC, new InternetAddress(aToEmailAddr));
			//message.addRecipient(Message.RecipientType.BCC, new InternetAddress(aToEmailAddr));
			message.setSubject(subjectLine);
			message.setText(body);
			Transport.send(message);
		
		} catch( MessagingException e ) {
			LOGGER.error("Error while sending email", e);
		}
	}
	
	public void refreshConfig( Properties mailServerConfig ) {
		this.mailServerConfig.clear();
		this.mailServerConfig.putAll( mailServerConfig );
	}
	
	public void refreshConfig( String propertyFilePath ) {
		mailServerConfig.clear();
		loadConfig( propertyFilePath );
	}
	
	private void loadConfig( String propertyFilePath ) {
		InputStream input = null;
		try {
			input = new FileInputStream(propertyFilePath);
			mailServerConfig.load(input);
		
		} catch( IOException e ) {
			LOGGER.error("Cannot open and load mail server properties file", e);
			
		} finally {
			try {
				if( input != null )
					input.close();
			} catch (IOException e) {
				LOGGER.error("Cannot close mail server properties file", e);
			}
		}
	}

}