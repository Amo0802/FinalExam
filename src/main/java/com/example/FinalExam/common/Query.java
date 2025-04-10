package com.example.FinalExam.common;

public interface Query <I, O>{
    O execute(I input);
}