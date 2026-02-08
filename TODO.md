# TODO

## Bugs
- Prevent cursor from recentering when opening the Baubles inventory.
----------------------
## Cleanup Tasks
- Clean up shiftclick logic
    - Move unequip to slot?
- Better stackable bauble handling
---------------------
## Feature Tasks
- Rendering
  - Debug renderer should become translucent.
  - First-person arm rendering

---------------------
## Ideas to be evaluated
- Data-driven slots
  - Parent data-driven slots to the root slots.
  - When slots are present, turn the parent slot into a button and populate the right side of the inventory.
  - 12 Slots per parent max, including the base slot(s).
  - Allow a slot count to be increased.
- Baubles combination
  - Data-driven, recipe and appearance.
  - Behaviour simply combines all logic of the two items.