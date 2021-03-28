# SignTech Sign Language Translator

"Signs are to eyes what words are to ears." - Deaf Proverbs Book Writer

An application developed by Manuel James B. Morillo, Ciandra V. Pancho, Melchizedek B. Recalde, James Bonnel E. Reyes (students from the Philippines)

## Inspiration
One necessary skill of a human is to communicate. Communication is an important management function that bridges that gap between individuals and groups through flow of information and understanding. This is where disabled people, especially deaf and mutes, are marginally separated from others. According to _NIDCD Health Statistics_ , about _2 to 3 out of every 1000 children in the United States_  are born with a detectable level of hearing loss in one or both ears. This just says there are lots of people marginally separated from communicating to others.

but Thanks to sign language existence, the world have become more open to understanding our deaf and mute friends, but the problem is only small amount of people know how to do and comprehend sign language. This is the reason why we have come up to our hackathon mobile application named as **SignTech**.

## What it does
The application SignTech is divided into 4 parts: learn section, maps, sign language translation, and speech-to-text.

First is the **Learn Section**, the user who has no knowledge of Sign Language could _learn through Youtube tutorials and articles_ provided inside this section of the application. There are also links to different websites that offer paid and free online courses about sign language learning. 

Second is the **Maps**, in this section, the user could _look for businesses that employ mute and deaf people_ so that he/she can help this kind of business, basically finding businesses that are deaf and mute-friendly near his/her location.

The third is **Sign Language Translation**, with the use of a phone camera, the device is able _to detect the message behind the sign languages_ a deaf or mute person is performing. The sign languages will be translated to text on the screen and will be read by Google Speech to help the unread and also the blind people to still comprehend what their deaf or mute friends are saying by hearing the message. This section aims to help abled users and blind users to understand their deaf and mute friends.

The fourth and last section is the **Speech-to-Text**, in this section, the user will just press the microphone at the center of the screen and _speak the message he would like to say to his deaf and mute friend_, and automatically the message will be displayed on screen to make their deaf and mute friend read the message spoken. This section aims to help abled user to convey their message to their deaf and mute friends.

## How we built it
For this application, we used two Google Cloud Platform APIs namely **Maps API** and **Location API**, which used in the second section of the application. As for the language, we used  **Kotlin**. As for the collaboration, we used **Git and Github**. We used **Teachable Machine** for the gathering of data (images) for our model. Also, we used **Tensorflow Lite** and **Tensorflow Lite Model Maker** to create our custom model. For us to make our camera app development easier and to access a buffer seamlessly for use in our algorithm, such as to pass into MLKit, we used **CameraX**. Moreover, we used **MLKit Vision API (Object Detection and Tracking)** to help us in the processing of camera input/stream. For the Youtube videos in our learn section, we used the Youtube Android Player API. And since this is a mobile application, we used **Android Studio** as our IDE or coding environment. 

## Challenges we ran into
During the planning process, we struggled about where to get the resources, especially the APIs, but thanks to Hoohacks, we found out about GCP. During the coding, we struggled with using fragments as our structure design for the mobile application and so we decided to just use separate activity files. 
We also struggled on the models since the samples have to be really trained hardcore to come up with a high level of confidence in terms of accuracy. Android Studio crashes sometimes. There were difficulties with converting Java language to Kotlin. Kotlin pre-release update of the compiler cannot compile some code units.

## Accomplishments that we're proud of
As for our application, we are really proud that we are able to create an application like this in the field of _machine learning or artificial intelligence_ for the very **FIRST TIME**. We have come up with a very useful tool for individuals in real life, and it makes us really happy! Through this hackathon, we were able to learn more about different resources that we can use in our projects, structures, algorithms, and many more.

As for our HooHacks experience, all of us are first-timers, we never thought a hackathon would be really so fun, there were lots of learnings presented by different sponsors and also fun activities that boosted us up. We discovered about **Google Cloud Platform APIs** like _Maps, Places, and also the Firebase, etc._ We learned more about the language **Kotlin**. We are very happy that we got to learn more about **Django Python framework, React, and Node.js..** We were also taught using **GIT commands and Github** that we found really helpful for life. We believe that this is part of our accomplishment to learn more about technology.

## What we learned
We are really proud that we are able to create an application like this in the field of machine learning or artificial intelligence for the very **FIRST TIME**. We have come up with a very useful tool for individuals in real life, and it makes us really happy!

## What's next for SignTech
We hope that in the future, we get to **increase the number of data (images)** by including all the sign language movements to make it complete. We also want to improve the **accuracy of the detector**. Moreover, we want to improve our **UI and UX**. 
