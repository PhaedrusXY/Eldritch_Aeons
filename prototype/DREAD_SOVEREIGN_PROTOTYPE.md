# Dread Sovereign encounter prototype

This branch contains the testable NeoForge 26.1.2 prototype of the Dread Queen / Black Frost Final Castle encounter.

## Current test build: 0.1.3

Version 0.1.3 is a loading and friendly-fire hotfix for 0.1.2:

- removes the fragile vanilla `EntityAllianceMixin` and `LivingEntityAllianceMixin` classes;
- retains only the known-good Ice and Fire `DragonBaseEntityMixin` passenger guard;
- moves encounter alliance enforcement into NeoForge damage events and per-tick encounter logic;
- cancels direct friendly fire among Black Frost, the Queen, and registered Dreadbound summons;
- cancels attacker-less delayed/environmental damage to the Queen and Dreadbound summons, including delayed frost damage;
- clears freeze accumulation and redirects allied AI targets every encounter tick.

Implemented encounter behavior:

- non-consuming Occultism ritual using all eight Twilight Forest boss trophies;
- stage-five Ice and Fire dragon-skull catalyst validation;
- mounted Dread Queen and fixed silver stage-five Black Frost opening;
- forced dragon-first transition and repeatable resurrection while the Queen lives;
- independent paired phase with Dread summons and local Build A Spell-style abilities;
- unbound, permanently killable Black Frost finale after the Queen dies;
- operator commands for rapid phase testing and cleanup.

The cinematic camera, player lock, recorded voice work, expanded resurrection presentation, final model/renderer, and balance pass remain deferred.

Replace all earlier Dread Sovereign prototype JARs. Install only the newest version.
