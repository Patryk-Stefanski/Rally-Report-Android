# Rally-Report-App (Android)

## Description 
Android application written in Kotlin, using Firebase Cloud Storage and Authenticate for a persistant database user management.
Designed for rally fans to create posts about competitors at specific location that can be marked on google maps

## Features 
- Account regisration and authentication allows for multiple users including login with google.
- CRUD functionality on all models (described below)
- Ceratin Features locked to only admin accounts

## Models

### Post
Post is a model to keep track of all the rally posts it icnludes title, description, long/lat on map, image and image ref for loading images.

### Event
Event is a model to keep track of all the rally events it includes the event name, start date, end date and list of competitors.

### Competitor
Competitor is a model to store all the competitors it includes the drivers/navigators first and last name their car number and car description.
