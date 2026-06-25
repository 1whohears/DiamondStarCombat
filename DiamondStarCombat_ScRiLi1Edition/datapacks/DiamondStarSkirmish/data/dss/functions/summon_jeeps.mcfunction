# summon a jeep at every jeep marker

execute as @e[tag=spawn_jeep] at @s unless entity @e[tag=jeep,distance=..5] run summon dscombat:car ~ ~ ~ {preset:willy_jeep,Tags:["jeep","vehicle"]}

# rotate the jeep in the same direction as the marker

execute as @e[tag=spawn_jeep] at @s rotated as @s run tp @e[tag=jeep,limit=1,sort=nearest] ~ ~ ~ ~ 0