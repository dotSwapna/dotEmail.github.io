package dot.demo.email;

import java.util.Arrays;
import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class EmailDto {

	@NotNull (message = "From email address cannot be null")
	@Email (message = "From email address is not valid")
	private String from;

	@NotEmpty (message = "To email address cannot be empty")
	@Email (message = "To email address is not valid")
	private String [] to;

	
	@Email (message = "Cc email address is not valid")
	private String [] cc;
	
	@Email (message = "Bcc email address is not valid")
	private String [] bcc;

	@NotNull (message = "Email subject cannot be null")
	private String subject;

	@NotNull (message = "Email message cannot be null")
	private String message;

	private boolean isHtml;
	
	private boolean isTemplate;
	
	private boolean hasAttachment;
	
	private String pathToAttachment;
	
	private String attachmentName;
	
	private String templateName;
	
	private String templateLocation;
	
	private Map<String, Object> parameterMap;
	
	private Map<String, Object> staticResourceMap;
	
	private String emailedMessage;
	
	

	public EmailDto() {
		
	}

	public EmailDto(String from, String toList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to = splitByComma(toList);
	}

	
	public EmailDto(String from, String toList, String ccList) {
		this();
		this.from = from;
		this.to = splitByComma(toList);
		this.cc = splitByComma(ccList);
	}
	
	public EmailDto(String from, String toList) {
		this();
		this.from = from;
		this.to = splitByComma(toList);
	}
	
	public EmailDto(String from, String toList, String ccList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to = splitByComma(toList);
		this.cc = splitByComma(ccList);
	}

	// getters and setters not mentioned for brevity

	private String[] splitByComma(String toMultiple) {
		String[] toSplit = toMultiple.split(",");
		return toSplit;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String [] to) {
		this.to = to;
	}

	public String [] getCc() {
		return cc;
	}

	public void setCc(String [] cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public String getPathToAttachment() {
		return pathToAttachment;
	}

	public void setPathToAttachment(String pathToAttachment) {
		this.pathToAttachment = pathToAttachment;
	}


	public boolean isTemplate() {
		return isTemplate;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public boolean isHasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateLocation() {
		return templateLocation;
	}

	public void setTemplateLocation(String templateLocation) {
		this.templateLocation = templateLocation;
	}

	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String getEmailedMessage() {
		return emailedMessage;
	}

	public void setEmailedMessage(String emailedMessage) {
		this.emailedMessage = emailedMessage;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}


	/**
	 * @return the staticResourceMap
	 */
	public Map<String, Object> getStaticResourceMap() {
		return staticResourceMap;
	}

	/**
	 * @param staticResourceMap the staticResourceMap to set
	 */
	public void setStaticResourceMap(Map<String, Object> staticResourceMap) {
		this.staticResourceMap = staticResourceMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailDto [" + (from != null ? "from=" + from + ", " : "")
				+ (to != null ? "to=" + Arrays.toString(to) + ", " : "")
				+ (cc != null ? "cc=" + Arrays.toString(cc) + ", " : "")
				+ (bcc != null ? "bcc=" + Arrays.toString(bcc) + ", " : "")
				+ (subject != null ? "subject=" + subject + ", " : "")
				+ (message != null ? "message=" + message + ", " : "") + "isHtml=" + isHtml + ", isTemplate="
				+ isTemplate + ", hasAttachment=" + hasAttachment + ", "
				+ (pathToAttachment != null ? "pathToAttachment=" + pathToAttachment + ", " : "")
				+ (attachmentName != null ? "attachmentName=" + attachmentName + ", " : "")
				+ (templateName != null ? "templateName=" + templateName + ", " : "")
				+ (templateLocation != null ? "templateLocation=" + templateLocation + ", " : "")
				+ (parameterMap != null ? "parameterMap=" + parameterMap + ", " : "")
				+ (emailedMessage != null ? "emailedMessage=" + emailedMessage : "") + "]";
	}

	
	
	
	

}
