# KnowYourGovernment

<b>Description</b>
- Uses location services to download local, state, and nation elected official contact information for current zipcode
- Uses Google Civic Information API (https://developers.google.com/civic-information/)
-

<b>Installation</b>
- USB Cable
- Android device or emulator with minimum SDK 21
- Android Studio or equivalent IDE


<b>Usage</b>

Upon app installation and initial launch your device will use location services to get a zipcode and asynchronously download official information from the Google Civic API. This information is then loaded into a list which is displayed in the main activity.


<p align="center">
  <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/initial_launch.png" height="550" >
</p>

Clicking on a list item opens a new activity with additional elected official information

