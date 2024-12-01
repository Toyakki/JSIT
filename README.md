## JSIT

Authors: Isaac Clark, Enze Yang, Tohya Tanemura, and Sark Asadourian

## Summery
JSIT is an open source simple tool for creating, submitting, and grading assignments 
for teachers and students which allows for the easy expansion of functionality.

Current open source assignments submission and grading solutions are based on 
complicated code bases and require complex cloud set up. This slows down educational
technology professionals in creating learning solutions tailored to their institution's
unique learning need.

To solve the problem of complicated assignments submission and grading solutions 
we implemented an system with an intuitive architecture utilizing a code architecture
which allows for expansion which allow for the easy addition of new features. In addition,
we utilize dropbox creating a simple set up for cloud functionality.

## Table of Contents
- [Features](#Features_id)
- [Installation](#Installation_id)
- [How to Use](#HowTo_id)
- [Feedback](#Feedback_id)
- [Contribution](#Contribution_id)


<a id="Features_id"></a>
## Features
- Creation of courses by teachers to organize courses.
- Creation of assignments through uploading a PDF, setting total marks, and due date.
- Submission of assignments by uploading PDF from computer.
- Grading of assignments by uploading  PDF with feedback from computer and entering grade.
- Students can view all of their assignments for a course in one place.
- Teachers have a view of who completed the assignment and who's assignments have been graded.

<a id="Installation_id"></a>
## Installation
- Clone repository into an IDE with OpenJDK 23 set as SDK.
- Apache Maven must be installed and the project in the IDE must be set to a Maven project.
  TODO Dropbox
- Mark the "java" folder as source root.
- Go to the "Main" file and run Main.main().
- From here you can create an account using the UI.

**Link to download:**
OpenJDK 23: https://jdk.java.net/23/
Apache Maven 3.9.9: https://maven.apache.org/

Note: This project has been only tested on MacOS and Windows but not Linux.

<a id="HowTo_id"></a>
## How to Use
TODO video

<a id="Feedback_id"></a>
## Feedback
Use this Google Form to send feedback:
https://docs.google.com/forms/d/12HE93oHGp-toP4sJcvSRnPij8snQBpyGElFsXGJ1h1A/prefill

All feedback is appreciated but implementation of feedback will be only for bugs when they
are able to be reproduced. Ensure bug reports have details on how to be reproduced. Expect at
most a month for bugs to be fixed.

<a id="Contribution_id"></a>
## Contribution
You are free to make forks of this project but no merge request will be excepted at this time.

Team Members:
- Isaac Clark     - theflickerman
  - theflickerman404
- Enze Yang       - yeddddy
- Tohya Tanemura  - Toyakki
- Sark Asadourian - sark-adadourian

User stories:
---current---
Isaac
- As a teacher, I want to create an assignment with a due date, PDF, and a total # of marks
- Upload Interface and Use Case

Tohya
- As a student, I want to submit by PDF assignment
- As a teacher, I want to grade assignment as downloading it, then uploading an PDF, and setting grade
- Download Interface and Use Case

Sark
- As a student, I want to view my assignment that my teacher submitted
- As a student, I want to view my grade and download my graded PDF

Jed
- As a teacher, I want to create a class with a name
- As a student, I want to join a class with class code

----old----

- As a student, I want to create an account with a strong password and email so that I can join a class as my teacher
  explained. (team)

- As a student, I want to join a class with the code given by my teacher so I can submit assignments.

- As a student, I want to log back into my account and type in my password and username again, so I can see the
  assignments I have. (team)

- As a professor, I want to create an account with a strong password and email so that I can create a class. (team)

- As a professor, I want to create a class for my students with a name and code that is given for the students to type
  in, so I can assign work. (team)

- As a professor, I want to create a new assignment submission system for a new course so that I can organize homework
  assignments easily. (Tohya)

- As a professor, I want to be able to upload a document of one question and create the assignment with a message/title
  so that I can assign homework whenever I want and set the deadline to any date I think is appropriate. (Jed)

- As a student, I want to submit my homework from the computer which I have already filled out so that I can get a grade
  (Sark)

- As a professor, I want to be able to easily upload my students’ grades and feedback to my class so that they can
  access it. (Isaac)

- As a student, I want to be able to check my grades and recent feedback. (Isaac)
