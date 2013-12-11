team03
======

SimCity201 Project Repository for CS 201 students

Please see the Wiki for instructions on running SimCity201.


###Working components of v1
  + Josh's restaurant is working
  + Unit testing of ALL agents
  + A person can go into a house, sit in a chair, and leave the house
  + A person can go into the bank, and stay there
  + A person can switch roles, has a script to determine which roles to take on
  + The animation only works when you click inside a building

###Not working components of v1
  + Transportation: There are no cars or buses because Alfred left in the middle of the project. Wilcynski said that we do not need to worry about this aspect of the project because of that.
  + Bank returns a null pointer error halfway through running
  + Reduced functionality: only one restaurant
  + Restaurant works, but food is never put on plating area
  + ResidentGui does not work in certain cases



###Working components of v2
  + All housing scenarios work
  + Housing movement algorithms work
  + All bank scenarios work
  + All market scenarios work
  + Person in the city movement algorithm works
  + Traffic right-of-way works (and so does the non-norm of busses running people over)
  + People can ride buses
  + Banks are closed on weekends; Landlords collect rent only on Saturdays
  + All animation continues even when building interiors aren't visible
  + All tests work

###Not working components of v2
  + Banks, markets, and all restaurants lack effective movement algorithms
  + No businesses incorporate banking (banking is fully capable, just no *individual* had time to incorporate that into their restaurant)
  + Cherys' restaurant lacks producer-consumer waiters
  + Jesus' restaurant lacks producer-onsumer waiters
  + Jesus' restaurant can interact with the market, but the deliverer never comes back from his restaurant, so we made it so his restaurant never orders from the market
  + Anjali's restaurant lacks producer-onsumer waiters
  + Sometimes the program freezes when there are too many people


###How to run our simcity
  + Run SimCity.java
  + Select a config file using the panel on the left, click the radio button, then cick the run scenario button. Close the program after running each scenario. Run it again choosing a different scenario.

####Different Scenarios
  + Normative A Config runs Normative Scenario A (also scenario C for restaurants that are able to interact with the market)
  + Normative B Config runs Normative Scenarios B and E
  + Non-Norm F Config runs Non-Norm Scenarios F, G, Q, and R
  + Non-Norms Config runs Non-Norm Scenarios Q and S
  + Bank Config runs Non-Norm Scenario O
  + Full City Config runs Normative Scenario J (since there are so many people, the animation often froze on our machines)

###Notes
  + For some reason, Anjali was unable to compile and run the program on her Mac, so she made changes on her teammates computers that were related to bank
  + Everyone worked super hard, everyone pulled their weight, Josh was an amazing team leader!
  + Since the person originally meant to do our transportation dropped (?) we were told that we only needed busses and the bus hitting pedestrian non-norm
  
###Work Break Down
  + Anjali--Banks and restaurant
  + Cherys--Housing and restaurant
  + Jesus--Markets and restaurant
  + Josh--Person, Busses, City Operation Structure, and restaurant (Josh gets a gold star)
  + Everyone--everything else + odd jobs

