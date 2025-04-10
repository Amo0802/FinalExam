package com.example.FinalExam.common;

public interface Command <I, O>{
    O execute(I input);
}
