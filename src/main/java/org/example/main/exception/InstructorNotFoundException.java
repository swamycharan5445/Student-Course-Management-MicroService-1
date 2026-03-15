package org.example.main.exception;

public class InstructorNotFoundException extends RuntimeException
{
    public InstructorNotFoundException()
    {
        super();
    }
    public InstructorNotFoundException(String message)
    {
        super(message);
    }
}
