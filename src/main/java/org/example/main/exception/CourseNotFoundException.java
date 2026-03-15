package org.example.main.exception;

public class CourseNotFoundException extends RuntimeException
{
    public CourseNotFoundException()
    {
        super();
    }
    public CourseNotFoundException(String message)
    {
        super(message);
    }
}

