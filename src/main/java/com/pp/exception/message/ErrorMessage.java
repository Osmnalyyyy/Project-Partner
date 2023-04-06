package com.pp.exception.message;

public class ErrorMessage {
public final static String RESOURCE_NOT_FOUND_MESSAGE="Resource with id %d not found";
public final static String ROLE_NOT_FOUND_MESSAGE="Role: %s not found";
public final static String USER_NOT_FOUND_MESSAGE="User with email %s not found";
public final static String JWTTOKEN_ERROR_MESSAGE="Jwt Token Validation Error %s";
public final static String EMAIL_ALREADY_EXISTS_MESSAGE="Email: %s already exists";
public final static String PRINCIPAL_FOUND_MESSAGE="User not found";
public final static String 	NOT_PERMITTED_METHOD_MESSAGE="You don't have any permission to change this data";
public final static String 	PASSWORD_NOT_MATCHED="Your passwords are not matched";
public final static String 	IMAGE_NOT_FOUND_MESSAGE="ImageFile with id %s not found";
public final static String 	IMAGE_USED_MESSAGE="ImageFile is used by other project";
public final static String 	ATTACHMENT_NOT_FOUND_MESSAGE="AttachmentFile with id %s not found";
public final static String 	ATTACHMENT_USED_MESSAGE="AttachmentFile is used by other project";
public final static String PARTICIPANT_NOT_FOUND_MESSAGE="Resource with id %d not found";

public final static String MAX_SHARE_LIMIT_REACHED_MESSAGE="Receivable limit of this project is = %d. Your request is %d";
public final static String PROJECT_NOT_FOUND_MESSAGE="Project with id %d not found";

public final static String PROJECT_CAN_NOT_BE_DELETED_MESSAGE="Project with id = %d have participants. You can not delete.";



    public final static String PROJECT_NOT_OWN_MESSAGE="Project with id %d not own user";
}
