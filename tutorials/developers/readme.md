# SocNet SDK v3 Tutorial for Developers
This implementation requires you to run the library (*.jar) to be executed.

You've to understand general concept of these main points:
1) OOP Concept

2) Method

3) Thread


## Comprehensive Tutorials.
**Power Point**
1. [English](tutorials-developers-english.pptx).

2. [Bahasa](tutorials-developers-indonesia.pptx).


**PDF**
1. [English](tutorials-developers-english.pdf).

2. [Bahasa](tutorials-developers-indonesia.pdf).


## Quick Tutorials.
First you have to put the *.jar file under your Project. Then you may use the following code :

### - Basic Simulation
We're going to :
1) Logging to Facebook

2) Publishing a post

Here is the code:
```
Engine myBot = new Engine();
Account myUser = new Account("satu@gmail.com”, “konci123”, SocialMedia.FACEBOOK);
myUser.addPost(“Cobalah SocNetSDK ada di github koq!”);
myBot.addUser(myUser);
myBot.start(true);
```

Only 5 lines of code, have you tried it before? The last line whereas **true** parameter given will effecting the Frame to be automatically closed when all works have been executed.


### - Challenging Simulation
We're going to :
1) Logging into 2 Different Social Medias

2) Publish the same post


So, here is the code:
```
Engine myBot = new Engine();
Account myUsers []= AccountBuilder.create("one@gmail.com", "konci", “hayo coba socnetSDK!”,
SocialMedia.FACEBOOK, SocialMedia.TWITTER);
myBot.addUsers(myUsers);
myBot.start(true, 3, 7);
```

We've just used **AccountBuilder** for creating account into **different social medias**. 

And take a look for the last line of code, we put **true** for auto-closing-frame, and then **interval-min** which is **3** and **interval-max** which is **7**. 

What's that means anyway? **SocNetSDK** will have a time to wait within several minute either 3-7 minutes gap. So your activity in social medias are good to go for future usage!

That's it!