execute as @a[tag=super_jump] run push 0.6 0 1 0
execute as @a[tag=super_jump] at @s run playsound minecraft:entity.player.splash.high_speed ambient @s ~ ~ ~ 1 1.4
execute as @a[tag=super_jump] run tag @s remove super_jump