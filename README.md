# CSCI 3130 - Software Engineering, Winter 2018
This project is a group project for the Winter 2018 offering of Software Engineering.

# Overview

The project will consist of the development of an athlete monitor application that allows users to develop self-administered, and coach-driven training routines.  The application is primarily an exercise routine monitor and communication tool.

# Statistics

[![Waffle.io - Columns and their card count](https://badge.waffle.io/NicholasBarreyre/SEProject.svg?columns=To%20Do,In%20Progress,Ready%20For%20Review,Done)](https://waffle.io/NicholasBarreyre/SEProject)

[![CircleCI](https://circleci.com/gh/NicholasBarreyre/SEProject/tree/master.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/NicholasBarreyre/SEProject/tree/master)

# Stable Features

No iterations have been released.

# Iteration 0

Iteration 0 includes the software engineering activities required to seed the agile development cycle.  It includes:

1. Setting up the Github environment;
1. Integrating Waffle;
1. Setting up team communication in Slack;
1. Ensuring Android Studio is functional for all team members;
1. Setting up project management repository; 
1. Development of initial feature list;
1. Development of initial list of User Stories.

# Iteration 1 Planned Features

Iteration 1 features will include:

1. Account registration
1. Login/Logout 
1. Activity creation
1. Record activity information
1. Manual input of activity data
1. Personal statistics
1. Team creation

# Iteration 2 Planned Features

TBD

# Iteration 3 Planned Features

TBD

# Team
The project team consists of (in alphabetical order, by last name):

* Victor Bares
* Nicholas Barreyre
* Nick Burris
* Brent Crane
* Elliott Darbyshire
* Lee Lunn
* Zach Wilkins

# Tool Stack
The following tools are used in the development lifecycle:

* [Android Studio](https://developer.android.com/studio/index.html)
* [GitHub](https://github.com/)
* [Waffle](https://github.com/waffleio/waffle.io)
* [Google Drive](https://www.google.com/drive/)
* [Slack](https://slack.com/)
* [Espresso](https://developer.android.com/training/testing/espresso/index.html)
* [Firebase](https://firebase.google.com/)
* [CircleCI](https://circleci.com/)

# Issues Tracking

The team uses [Waffle](https://waffle.io/NicholasBarreyre/SEProject) to manage workflows within GitHub Issues and Milestones.  In addition to Waffle, for tracking ongoing development and release cycles, other labels are used that are based on the [Sane GitHub Labels](https://medium.com/@dave_lunny/sane-github-labels-c5d2e6004b63) approach, with certain adaptations adopted from the open source [TEAMMATES](https://github.com/TEAMMATES/teammates) project.

Of the labels used that are not managed by Waffle, there are three main categories:

1. Priority: Urgency of the issue as it relates to Milestone and dependence of other issues. 
   1. Urgent: Issues that are top priority and critical to a Milestone and/or issues with dependency.
   1. High: Issues that have affect the functionality and/or ability to continue development of other issues.
   1. Medium: Issues that have no impact on other issues, but are important to achieving a milestone.
   1. Low: Issues that have no impact on other issues, do not impact the logical correctness of a feature, or may be pushed to a different milestone

1. Status: What state an issue is currently in.
   1. Abandoned: Removed from the current iteration, and should be reviewed for inclusion in the next iteration.
   1. Accepted: Accepted for inclusion in the current iteration.
   1. Complete: Completely integrated into the master branch, and no further work should be required.
   1. In Progress: Currently being handled by the assigned team member.
   1. In Review: Under code and correctness evaluation.
   1. Merge: Issue reviewed, passes integration and unit testing, and is waiting to be merged.
   1. On Hold: Waiting for progress required with other isues, or awaiting assessment for being abandoned.
   1. Review Needed: Unit and integration tests pass, awaiting for code and correctness evaluation.

1. Type: The type of issue
   1. Bug: Issue related to a bug in another issue
   1. Enhancement: Proposed change to a feature to improve it's design
   1. Project Management: Issues or tasks not directly related to code, but instead focused on coordination of project management
   1. Question: Questions about code, features, or other related things.
   1. Refactor: Code that has been refactored from a previous version.
   1. Unit Test: Code related to unit tests that have been written.
   1. User Story: User stories associated with a Milestone.  All other issues, except project management, should be associated with one or more user stories.
