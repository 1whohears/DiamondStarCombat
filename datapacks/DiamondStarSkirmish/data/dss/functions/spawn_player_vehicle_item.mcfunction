# spawns the selected player's vehicle item in their inventory at the nearest spawn_vehicle marker

execute as @e[tag=spawn_vehicle,distance=..200] at @s unless entity @e[tag=vehicle,distance=..5] run tag @s add no_vehicle
execute as @s at @e[tag=spawn_vehicle,tag=no_vehicle,sort=nearest,limit=1] run summon_vehicle_item ~ ~ ~ ~ 0 {Tags:["vehicle"]}
execute as @e[tag=spawn_vehicle,distance=..200] at @s if entity @e[tag=vehicle,distance=..5] run tag @s remove no_vehicle