# KnowYourGovernment

<b>Description</b>

Android App that uses location services and the Google Civic Information API (https://developers.google.com/civic-information/) to download and display contact information for local, state, and national elected officials based on the device's current location or a user entered location.

<b>Installation</b>
- USB Cable
- Android device or emulator with minimum SDK 21
- Android Studio or equivalent IDE

<b>Usage</b>

<div class="row">
    <p>Upon app installation and initial launch your device/emulator will use location services to get your current zipcode and asynchronously download official information from the Google Civic API. This information is then loaded into a list which is displayed in the main activity.
    </p>
    <p align="center" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/initial_launch.png" height="400" >
    </p>
</div>


<div class="row">
    <p>Clicking on a list item opens a new activity with additional elected official information.</p>
    <p align="center" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/example_dem.png" height="400" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/example_gop.png" height="400" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/example_ind.png" height="400" >
    </p>
</div>

<div class="row">
    <p>Clicking on the picture will open an activity with a larger version of said picture.</p>
    <p align="center" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/photo_activity.png" height="400" >   
    </p>
</div>


<div class="row">
    <p>All three activities also work in landscape.</p>
    <p align="center" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/main_act_land.png" height="200" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/example_ind_land.png" height="200" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/photo_act_land.png" height="200" >
    </p>
</div>

<div class="row">
    <p>To search for official information for somewhere else click the search icon in the top left.</p>
    <p align="center" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/initial_launch.png" height="400" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/search_dialog.png" height="400" >
    </p>
    <p>Enter a City, State, or Zipcode and then select OK, at which point the list will be reloaded.</p>
    <p align="center">
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/search_dialog_nj.png" height="400" >
      <img src="https://github.com/annalbenson/KnowYourGovernment/blob/master/screenshots/new_location_nj.png" height="400" >
    </p>
</div>
