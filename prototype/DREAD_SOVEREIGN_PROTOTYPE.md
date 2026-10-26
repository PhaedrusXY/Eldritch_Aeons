# Dread Sovereign encounter prototype

This branch contains the testable NeoForge 26.1.2 prototype of the Dread Queen / Black Frost Final Castle encounter.

The complete Gradle source project is stored as checksum-verified base64 chunks; the branch workflow reconstructs it and builds the test JAR. This packaging keeps the prototype isolated from the released Eldritch Aeons pack while the encounter is being tested.

## Prototype 0.1.1 recovery build

The initial 0.1.0 test exposed two related integration problems. Ice and Fire CE does not register its unfinished Dread Queen entity, and a direct registry lookup therefore produced Minecraft's default pig entity. Mounting that placeholder on Black Frost also sent it through Ice and Fire's prey-in-mouth passenger code, which attempted a server-only cast on the client and crashed the game.

Version 0.1.1 changes the opening phase as follows:

- the temporary Queen visual is an explicitly resolved Dread Lich rather than a registry-default pig;
- the Queen is positioned above Black Frost by the encounter controller instead of becoming a true dragon passenger;
- a narrowly scoped mixin prevents the saved 0.1.0 Black Frost / placeholder-Queen pair from re-triggering the client crash;
- orphaned entities from interrupted prototype encounters are automatically removed when their saved chunks load;
- the remaining combat phase logic, ritual, commands, and resurrection prototype remain available for testing.

The Dread Lich is only a temporary visual stand-in. A dedicated Dread Queen entity, renderer, model, equipment presentation, and proper boss animation remain future work.

## Implemented encounter systems

- non-consuming Occultism ritual using all eight Twilight Forest boss trophies;
- stage-five Ice and Fire dragon-skull catalyst validation;
- simulated mounted opening with a fixed silver stage-five Black Frost;
- forced dragon-first transition and repeatable resurrection while the Queen lives;
- independent paired phase with Dread summons and local Build A Spell-style abilities;
- unbound, permanently killable Black Frost finale after the Queen dies;
- operator commands for rapid phase testing and cleanup.

The cinematic camera, player lock, recorded voice work, expanded resurrection presentation, final Queen art, and final balancing remain deliberately deferred.
