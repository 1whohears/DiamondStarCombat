This page is updated for v0.13.0. If anything is unclear please let me know in the discord so I can update this page!

# Basic Usage

A Wind Tunnel can be added to your world via the following command:

`/summon dscombat:wind_tunnel ~ ~10 ~`

You can manipulate various parameters in the wind tunnel using the following commands:

*All number values must be in decimal, otherwise Minecraft will turn `0` into `0.5` so you should input '0.0'*

*All Vehicle Preset Ids should be the unarmed variant.*

`/windtunnel set_all <vehicle_preset_id> <x_speed> <y_speed> <z_speed> <pitch> <yaw> <roll> <throttle(0-1)> <afterburner>`

`/windtunnel set_altitude <altitude>`

`/windtunnel set_hidden <hidden>`

`/windtunnel set_inputs <pitch_input(-1.0-1.0)> <yaw_input(-1.0-1.0)> <roll_input(-1.0-1.0)>`

`/windtunnel set_preset <vehicle_preset_id>`

# Designing a Flight Model

This new feature is probably took the most work to put together, but is is very important when designing flight models.
As we have seen so far, currently in DSC, a flight model is made up of Lift Surfaces (positions, size, rotations) and
Lift vs AOA/Drag vs AOA graphs. All of these properties need to be finely tuned so that the aircraft performs in the desired way.
Testing different coefficients for a bunch of different AOAs can be a really annoying and tedious task. The Wind Tunnel,
and a bunch of commands were created to automate this process as much as possible.

The Following Steps is the process I followed to design the Alexis Flight Model:

## 1) Determine Basic Stats

Either do research to find the real life stats of the Aircraft your custom vehicle is based on, or arbitrarily define them.

*Because the goal of this update is to make this mod a little more realistic, most of the stats that I compiled are based on IRL values.
It is no secret that the Alexis Plane is modeled after the F-16. So I used the F-16s real life empty weight, dry/wet thrust (off/on afterburner),
cruise/top speeds, wingspan, etc...[All the stats I found are compiled in this sheet.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/stat_graphs/Vehicle%20Stats.ods)*

## 2) Create a Turn Rate Chart

If you are modeling of a real life fighter jet, you will want to find EM diagrams.
These diagrams show the maximum sustained turn rate of an aircraft at various speeds.
You then need to find what AOA the Aircraft will be at during those sustained turn rates.

You will want to use LibreOffice/Excel to create a chart with 4 columns:
- **A**) IRL Speed (km/h)
- **B**) Scaled (1/8th IRL) in Game Speed (meters/second) (A*0.03472)
- **C**) Scaled in Game Speed (**meters/tick**) (B*0.05)
- **D**) Turn Rate (degrees/second)
- **E**) Angle of Attack (degrees)

*I used this website to compile turn rate performance data for the F-16, F-18, and Mig-29.*

*Full disclosure: Firefox started giving me a security warning before viewing for some reason recently.
It appears its HTTPS certificate expired in July 2025. So enter at your own risk.
Hopefully it gets fixed soon because it is a very nice resource.*

https://dcs.silver.ru

*However, the select data that I gathered from the site is viewable [in this sheet.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/stat_graphs/Vehicle%20Stats.ods)*

## 3) Use the Wind Tunnel Simulations to Create Lift/Drag Graphs

First set the `vehicle_preset_id` to your custom vehicle id, and ensure the throttle is maxed and the afterburner is on.

`/windtunnel set_all <vehicle_preset_id> 0.0 0.0 1.0 0.0 0.0 90.0 1.0 true`

Then you will need to input the data you collected (C, D, E). Each data point needs to be separated by a semi-colon,
and the number of data points needs to be the same for all three commands. Examples:

`/windtunnel set_speed_list "0.69;0.78;0.87;0.95;1.04;1.13;1.22;1.30;1.39;1.48;"`

`/windtunnel set_turn_rate_list "18.16;18.86;19.42;19.91;20.32;20.64;20.89;21.15;21.4;21.72;"`

`/windtunnel set_aoa_list "23.49;21.53;19.82;18.42;17.28;16.21;15.11;14.09;13.11;12.26;"`

Next, run the simulation with the following command. Note that by convention, Altitudes in game are 1/20th IRL Altitudes.
Thus, divide the altitude obtained from the EM charts you used by 20, and that is the Altitude you use in the command.

`/windtunnel find_multi_lift_drag <altitude>`

Depending on how many rows of data you give it, it may take a few minutes to complete.
Upon completion, it spits out the file path of where the output data is.

## 4) Update Your Lift/Drag Graphs

You will need to create two new Stat Graph json files: a `aoaliftk` for the Lift vs AOA graph, and a `floatfloat` for the Drag vs AOA graph.
The Lift Graph will need `mirror_negative_keys` and `invert_mirrored_values` to be set to `true`.
The Drag Graph will need just `mirror_negative_keys` to be set to `true`.

The values that the simulation spit out can be added to the `map` array in both Stat Graphs.
You will probably need to add a 0 to both maps, and some high drag values for high AOAs.
Check out the Eden [Lift vs AOA Graph](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_lift_aoa.json)
and [Drag vs AOA Graph.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_drag_aoa.json)
as examples.