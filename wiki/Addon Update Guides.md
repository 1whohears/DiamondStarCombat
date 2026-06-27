The following is primarily for Addon Devs to see what I changed so you can update/fix your addons accordingly.

# 0.13.0 -> 0.14.3

## Additional Vehicle Stats

- `min_drive_acc` | NUMBER | **0** | *The minimum ground acceleration applied to a vehicle when driving at full throttle. This can be used on any driving vehicle, but especially planes to help them reach take off speed faster.*

# 0.12.9 -> 0.13.0

- I unfortunately broke a lot of things in this one and I am going to try to be smarter about backwards compatibility in the future apologies.

- All speeds are in Meters per Tick! Apparently I failed to indicate this in the wiki before. Oops.
    - The exception to this is if `use_horizontal_speed_scale` is set to `true`. If `true`, the in game speed will be 1/8th the speed parameter in the preset file.
      The 1/8th scale value will be configurable in the future. Custom fighter jets should have this set to true.
    - There is also a `use_vertical_speed_scale` which scales down upward accelerations. This should just be set to true for all vehicles.

- Moving forward the goal is to use realistic mass values (in Kilograms) for all vehicles, and weapons.

- In general, `inertiapitch`, `inertiayaw`, `inertiaroll` should be set to much higher. Somewhere in the 10000 range for average sized vehicles.

- Manually setting the `presetId` within the JSON file is no longer required. The name of the JSON file is now the `presetId`.

## Fix Custom Vehicle Guide

Scroll down to New Properties to see the new stats that I will be referencing.

### Fix Custom Planes

Set `use_horizontal_speed_scale` and `use_vertical_speed_scale` to `true` if you want the planes to have speeds at 1/8th the IRL speed.
If so, set `max_ground_speed`, `cruise_speed`, and `max_speed` to the IRL speed in `meters/tick`.

Additionally, set `max_push_thrust_per_engine` to the IRL thrust in newtons.
If your plane uses an afterburner set `max_afterburner_push_thrust_per_engine` to the IRL thrust in newtons.

Set `break_deacc_ground` and `break_deacc_air` to some small non-zero value if you want to be able to use breaks. The default values are too small.

Set `drag_area` to some small value less than one. The induced drag caused by the wings and angle of attack will be the main source of drag.

Set `has_turn_assist` to `true` if you want your custom plane to have a Turn Assist/AOA Limiter. Modern jets should have this enabled.

Scroll down for more information on `physics_components`. This is how you can set the lift surfaces and configure the "flight model" of your aircraft.
The process of configuring a custom flight model is complex and time consuming. It will be completely detailed in the sections below.
You must first get familiar with how Lift Surfaces are defined, then learn how to make your own Lift vs AOA/Drag vs AOA graphs,
then learn how to use the Wind Tunnel simulation commands to find Lift/Drag coefficients that produce desired turn rates.

But you could simply inherit the fight model from another jet by using the `copyId` parameter, and then override all the other stats.
Or, you could just copy another jets set of `physics_components`. [Here is the Alexis Plane's for example.](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185)

### Fix Custom Cars

The `break_deacc_ground` parameter must be set in order for breaks to work.

### Fix Custom Tanks

The `break_deacc_ground` parameter must be set in order for breaks to work.
Additionally, one should set the `hard_coded_rot_acc` and `hard_coded_rot_decel` to easily allow tanks to turn. Here is an example block:

```
"hard_coded_rot_acc": {
    "x": 0.0,
    "y": 0.05,
    "z": 0.0
}
```

### Fix Custom Helicopters

The `heliLiftFactor` will likely need to be dramatically increased. On the Krait chopper for example, it is cuurrently set to 1500.
Additionally, one should set the `hard_coded_rot_acc` and `hard_coded_rot_decel` to easily allow helicopters to turn. Here is an example block:

```
"hard_coded_rot_acc": {
    "x": 0.1,
    "y": 0.1,
    "z": 0.1
},
"hard_coded_rot_decel": 0.1
```

### Fix Custom Boats

I apparently decided that boat breaks de-acceleration should be controlled by `break_deacc_air`? So you will need to set that parameter to some small non zero value.
Additionally, one should set the `hard_coded_rot_acc` and `hard_coded_rot_decel` to easily allow boats to turn (set Y so some small non-zero value).
One should also consider setting `max_push_thrust_per_engine` to some high value if the boat in question is very heavy.

### Fix Custom Submarines

I apparently decided that boat breaks de-acceleration should be controlled by `break_deacc_air`? So you will need to set that parameter to some small non zero value.
Additionally, one should set the `hard_coded_rot_acc` and `hard_coded_rot_decel` to easily allow submarines to turn (set XYZ so some small non-zero value).
One should also consider setting `max_push_thrust_per_engine` to some high value if the submarine in question is very heavy.

## New Properties

### Vehicle Stats

`stats` | JSON_OBJECT | **REQUIRED**

- `max_ground_speed` | NUMBER | **max_speed** | *The maximum speed the vehicle will travel while on the ground in `meters/tick`*

- `cruise_speed` | NUMBER | **max_speed** | *The max speed a vehicle can reach if the afterburner is OFF in `meters/tick`.
  `max_speed` is the speed a vehicle can reach if the afterburner is ON.*

- `use_horizontal_speed_scale` | BOOLEAN | **false** | *If `true`, all horizontal in game speeds will be 1/8th the speed parameters in this preset file.
  The 1/8th scale value will be configurable in the future. If you are making a custom fighter jet, set this to `true`, and make all speeds like `max_speed`
  and `cruise_speed` the real life speeds in `meters/tick` and also make the thrusts the real life values. Accelerations will be correctly scaled as well.*

- `use_vertical_speed_scale` | BOOLEAN | **false** | *If `true`, all vertical in game speeds will be 1/8th the speed parameters in this preset file.
  The 1/8th scale value will be configurable in the future. If you are making a custom fighter jet, set this to `true`, and make all speeds like `max_speed`
  and `cruise_speed` the real life speeds in `meters/tick` and also make the thrusts the real life values. Accelerations will be correctly scaled as well.*

- `max_afterburner_push_thrust_per_engine` | NUMBER | **max_push_thrust_per_engine** | *Afterburner thrust per engine in Newtons.
  If this is set to a value higher than `max_push_thrust_per_engine`, then the vehicle will be able to turn on an Afterburner.*

- `break_deacc_ground` | NUMBER | **0.005** | *The de-acceleration applied to a vehicle while using breaks on the ground in `meters/tick^2`*

- `break_deacc_air` | NUMBER | **0.001** | *The de-acceleration applied to a vehicle while using breaks in the air in `meters/tick^2`*

- `drag_area` | NUMBER | **cross_sec_area** | *The surface area of a vehicle used to calculate drag in `meters^2`.
  `cross_sec_area` is now separate and will be used for radar mechanics. Also note that drag for planes is more complex.*

- `has_turn_assist` | BOOLEAN | **false** | *Set to `true` if the pane should have a turn assist. Normally used for modern fighter jets.
  Also known as a 'Rate Limiter'.*

- `hard_coded_rot_acc` | VEC3 | **OPTIONAL** | *A rotational acceleration (`degrees/tick^2`) override for each
  rotational axis (X->pitch,Y->yaw,Z->roll) based on player inputs. Use in combination with `maxroll`, `maxpitch`, `maxyaw`, and `hard_coded_rot_decel`.
  This parameter is meant for all vehicles other than planes and cars (helis, boats, submarines, tanks).
  Note that cars should still used `turn_radius`.
  Finding the right `inertia` and `torque` values can be annoying for vehicles like boats that you just want to have simple turn mechanics.*

- `hard_coded_rot_decel` | NUMBER | **OPTIONAL** | *The rate in `degrees/tick^2` a vehicle will rotationally de-accelerate back to 0.
  See `hard_coded_rot_acc` for use cases.*

- `physics_components` | JSON_OBJECT_ARRAY | **OPTIONAL** | *An array of simulated physics instances that contribute to the vehicle's net forces and moments.
  One type of physics component is a lift surface. Scroll down for more information.
  Additionally, please look at [some examples](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185) to see how they are defined.*

- `plane` | JSON_OBJECT | **REQUIRED FOR PLANES** | *Stats that only planes used are stored here.*

    - `drag_aoa_graph_key` | STRING | **default_drag_aoa** | *A stat graph id. The drag vs aoa graph for the main plane body. Not as important as how the lift surfaces in `physics_components` are set up.*

    - `centripetal_scale` | NUMBER | **1** | *Use if you want to increase or decrease the centripetal forces cause by the plane's lift surfaces.*

#### `physics_components`

The stat property `physics_components` is an array of simulated physics instances that contribute to the vehicle's net forces and moments.
Please look at [some examples](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185) to see how they are defined.
See below for specifics on each Physics Component type.

The following are parameters that every Physics Component Type has:

`hitbox` | String | **NONE** | *If set to NONE, then this physics instance is active as long as the vehicle is operational.
One can set this to a hitbox name from `hitboxes` so that this physics instance is active as long as that hitbox is still alive/exists.*

`pos` | VEC3 | **OPTIONAL** | *The position in `meters` relative to the vehicle's origin that this physics instance operates. Assumes zeros if left empty.*

##### Lift Surface

Lift Surfaces will generate lift and induced drag for their parent aircraft based on Angle of Attack and velocity.

Planes now only rotate if there is a difference in forces among the lift surfaces. When all lift surfaces are stable, there is zero net torque.
When a lift surface rotates, the angle of attack for that surface becomes different than the rest, leading to a non zero net torque, causing rotation.
So the `input_type` parameter allows the lift surface to become a control surface by rotating it based on player inputs.

If you look at the [Alexis Plane's set of Lift Surfaces](https://github.com/1whohears/DiamondStarCombat/blob/32a8f38f8d6386f4a51ff15284503f520b44bd80/src/generated/resources/data/dscombat/vehicle/alexis_plane_empty.json#L185),
you will notice that where ever there is an `ELEVATOR` or `STABILIZER` on the tail side of the aircraft, there is an equally sized lift surfce on the
opposite side of the vehicle. This is to keep the aircraft stable if the pilot is not inputting anything.

`id` = `lift_surface`

`area` | NUMBER | **10** | *The surface area in `meter^2` of this lift surface.*

`rotation` | VEC3 | **OPTIONAL** | *The default rotation of the lift surface relative to the vehicle in degrees.
Leaving all as zero leaves the lift surface parallel with the ground.
If the lift surface is meant to be a tail stabilizer, then one should set the Z component to 90.*

`input_type` | ENUM | **NONE** | *Options: `NONE`, `LEFT_FLAP`, `RIGHT_FLAP`, `ELEVATOR`, `STABILIZER`.
The type of control surface. For example, `ELEVATOR` rotates up and down based on pitch inputs.
`STABILIZER` rotates based on yaw inputs, and the flaps rotate based on roll inputs.*

`input_rotation_max` | NUMBER | **4** | *How much the lift surface rotates in degrees when the maximum angular input is used.*

`ignore_roll` | BOOLEAN | **false** | *If `true`, the lift surface will not rotate on the roll axis when the plane rolls.
Use this to make a 'fuselage' lift surface so that a fighter jet can stay in the air even rolled 90 degrees.*

`lift_k_graph` | STRING | **fuselage** | *Lift vs AOA Stat graph id. Determines what Lift coefficient is used at the current AOA.
See the Stat Graph section for more information on these types of graphs.
See this [example Lift vs AOA graph for the Eden Plane.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_lift_aoa.json)*

`zero_lift_drag` | NUMBER | **0.5** | *Can be kinda thought of as the drag coefficient at 0 degrees AOA.*

`drag_graph` | STRING | **default_drag_aoa** | *Float vs Float Stat graph id. Determines what Drag Coefficient is used at the current AOA.
See the Stat Graph section for more information on these types of graphs.
See this [example Drag vs AOA graph for the Eden Plane.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_drag_aoa.json)*

### Stat Graphs

I apparently never documented the stat graph system so here we go.

Stat graphs are a way to define a property based on a current input stat. The graphs are 2D, so there are horizontal and vertical coordinates.
Theses coordinates are also known as Keys and Values.

For example, if one to look at an [Air Density Graph](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/air_density_minecraft_overworld.json),
the vehicle's current Y coordinate is the key, and the air density related to that Y coordinate is the value.
For example, in the Overworld Air Density Graph, a Y coordinate of 64 (Key) has an air density of 1.225 (value).

**Keys must be defined/sorted from least to greatest. The sorting of the values does not matter. The sorting must be done by Key.**

**Most Stat Graphs require a Key of Zero to be defined.**

If an input key is in between 2 defined keys, then the output value is linearly interpolated between the 2 values.
If an input key is above the highest key, then the value associated with the highest key will be returned.
If an input key is below the highest key, then the value associated with the lowest key will be returned.

All Stat Graphs have the following parameters:

`mirror_negative_keys` | BOOLEAN | **false** | *If `true`, input keys below zero will return the value associated with the positive input key.*

`invert_mirrored_values` | BOOLEAN | **false** | *If `true`, input keys below zero will return the negative value associated with the positive input key.*

#### Available Preset Types

- `floatfloat`
- `aoaliftk`
- `floatfloat_multi`
- `turn_rates_speed`

#### `floatfloat` Parameters

[Example graph using the `keys` and `values` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/air_density_minecraft_overworld.json)

[Example graph using the `map` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/alexis_drag_aoa.json)

`keys` | NUMBER_ARRAY | **OPTIONAL** | *A list of Keys. Must be the same length of `size` if used. An alternative way to create a graph is with `map`.*

`values` | NUMBER_ARRAY | **OPTIONAL** | *A list of Values. Must be the same length of `size` if used. An alternative way to create a graph is with `map`.*

`size` | NUMBER | **OPTIONAL** | *The length of the graph. REQUIRED IF defining the graph with the `keys` and `values` method.*

`map` | JSON_OBJECT_ARRAY | **OPTIONAL** | *An array of Json Objects where each entry contains a `key` and `value` property.*

#### `aoaliftk` Parameters

This graph type encodes the Lift Coefficient associated with Angle of Attack.

These graphs use the same properties as `floatfloat`.

[Example graph using the `keys` and `values` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/fuselage.json)

[Example graph using the `map` method.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/javi_lift_aoa.json)

#### `floatfloat_multi` Parameters

This graph type is similar to `floatfloat`, but only the `keys` and `values` method is available.
However `values` is a JSON_ARRAY of more NUMBER_ARRAYs containing the values.
This is because each key is associated with multiple values determined by `rows`.

`size` | NUMBER | **REQUIRED** | *The length of the graph.*

`rows` | NUMBER | **REQUIRED** | *The length of the graph.*

`keys` | NUMBER_ARRAY | **REQUIRED** | *A list of Keys. Must be the same length of `size` if used.*

`values` | JSON_ARRAY | **REQUIRED** | *A list of NUMBER_ARRAYs. Must be the same length of `rows` if used. Each sub NUMBER_ARRAY must be the length of `size`.*

#### `turn_rates_speed` Parameters

This graph encodes the maximum turn rate for each rotational axis based on the current speed in `meters/tick`.
The number of rows must be 3. Where Row 1 is Pitch, Row 2 is Yaw, and Row 3 is Roll.
These turn rates are used when the `dscombat:planeArcadeMode` gamerule is set to `true`, and when `turn_assist` is enabled.

These graphs use the same properties as `floatfloat_multi`.

[Example graph.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/e3sentry_turn_rates.json)

## The Wind Tunnel

### Basic Usage

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

### Designing a Flight Model

This new feature is probably took the most work to put together, but is is very important when designing flight models.
As we have seen so far, currently in DSC, a flight model is made up of Lift Surfaces (positions, size, rotations) and
Lift vs AOA/Drag vs AOA graphs. All of these properties need to be finely tuned so that the aircraft performs in the desired way.
Testing different coefficients for a bunch of different AOAs can be a really annoying and tedious task. The Wind Tunnel,
and a bunch of commands were created to automate this process as much as possible.

The Following Steps is the process I followed to design the Alexis Flight Model:

#### 1) Determine Basic Stats

Either do research to find the real life stats of the Aircraft your custom vehicle is based on, or arbitrarily define them.

*Because the goal of this update is to make this mod a little more realistic, most of the stats that I compiled are based on IRL values.
It is no secret that the Alexis Plane is modeled after the F-16. So I used the F-16s real life empty weight, dry/wet thrust (off/on afterburner),
cruise/top speeds, wingspan, etc...[All the stats I found are compiled in this sheet.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/stat_graphs/Vehicle%20Stats.ods)*

#### 2) Create a Turn Rate Chart

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

#### 3) Use the Wind Tunnel Simulations to Create Lift/Drag Graphs

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

#### 4) Update Your Lift/Drag Graphs

You will need to create two new Stat Graph json files: a `aoaliftk` for the Lift vs AOA graph, and a `floatfloat` for the Drag vs AOA graph.
The Lift Graph will need `mirror_negative_keys` and `invert_mirrored_values` to be set to `true`.
The Drag Graph will need just `mirror_negative_keys` to be set to `true`.

The values that the simulation spit out can be added to the `map` array in both Stat Graphs.
You will probably need to add a 0 to both maps, and some high drag values for high AOAs.
Check out the Eden [Lift vs AOA Graph](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_lift_aoa.json)
and [Drag vs AOA Graph.](https://github.com/1whohears/DiamondStarCombat/blob/1.19.2-dev/src/main/resources/data/dscombat/stat_graph/eden_drag_aoa.json)
as examples.