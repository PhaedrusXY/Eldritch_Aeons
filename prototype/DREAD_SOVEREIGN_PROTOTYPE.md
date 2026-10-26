# Dread Sovereign encounter prototype

This branch contains the first testable NeoForge 26.1.2 prototype of the Dread Queen / Black Frost Final Castle encounter.

The complete Gradle source project is stored as checksum-verified base64 chunks; the branch workflow reconstructs it and builds the test JAR. This packaging keeps the prototype isolated from the released Eldritch Aeons pack while the encounter is being tested.

Implemented in this pass:

- non-consuming Occultism ritual using all eight Twilight Forest boss trophies;
- stage-five Ice and Fire dragon-skull catalyst validation;
- mounted Dread Queen and fixed silver stage-five Black Frost opening;
- forced dragon-first transition and repeatable resurrection while the Queen lives;
- independent paired phase with Dread summons and local Build A Spell-style abilities;
- unbound, permanently killable Black Frost finale after the Queen dies;
- operator commands for rapid phase testing and cleanup.

The cinematic camera, player lock, recorded voice work, and expanded resurrection presentation remain deliberately deferred.
