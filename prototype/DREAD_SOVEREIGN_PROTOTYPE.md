# Dread Sovereign encounter prototype

This branch contains the testable NeoForge 26.1.2 prototype of the Dread Queen / Black Frost Final Castle encounter.

The complete Gradle source project is stored as checksum-verified base64 chunks; the branch workflow reconstructs it and builds the test JAR. This packaging keeps the prototype isolated from the released Eldritch Aeons pack while the encounter is being tested.

Implemented encounter behavior:

- non-consuming Occultism ritual using all eight Twilight Forest boss trophies;
- stage-five Ice and Fire dragon-skull catalyst validation;
- mounted Dread Queen and fixed silver stage-five Black Frost opening;
- forced dragon-first transition and repeatable resurrection while the Queen lives;
- independent paired phase with Dread summons and local Build A Spell-style abilities;
- unbound, permanently killable Black Frost finale after the Queen dies;
- operator commands for rapid phase testing and cleanup.

## 0.1.2 prototype fixes

- Black Frost, the Dread Queen, and summoned Dread creatures now share a no-friendly-fire scoreboard team. Ice and Fire checks this alliance before applying both dragon damage and its Frozen effect.
- Encounter maintenance clears any residual Frozen effect and accidental allied targeting.
- Black Frost now receives an upward flight recovery after 30 submerged ticks and an arena reset after 80 submerged ticks if Ice and Fire's swimming AI leaves him inert in water.

The cinematic camera, player lock, recorded voice work, expanded resurrection presentation, proper Queen model/entity, and final balance remain deferred.
