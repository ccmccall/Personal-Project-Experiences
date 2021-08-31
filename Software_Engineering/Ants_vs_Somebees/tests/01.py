test = {
  'name': 'Problem 1',
  'points': 2,
  'suites': [
    {
      'cases': [
        {
          'answer': 'ea8be1d7e2538df22f92d44f8cafb0b4',
          'choices': [
            r"""
            Placing an ant into the colony will decrease the colony's total
            available food by that ant's food_cost
            """,
            r"""
            Each turn, each Ant in the colony eats food_cost food from the
            colony's total available food
            """,
            r"""
            Each turn, each Ant in the colony adds food_cost food to the
            colony's total available food
            """
          ],
          'hidden': False,
          'locked': True,
          'question': 'What is the purpose of the food_cost attribute?'
        },
        {
          'answer': '4e249e6166a80ed8a7cc81ccb599da58',
          'choices': [
            'class, all Ants of the same subclass cost the same to deploy',
            'class, all Ants cost the same to deploy no matter what type of Ant it is',
            'instance, the food_cost of an Ant depends on the location it is placed',
            'instance, the food_cost of an Ant is randomized upon initialization'
          ],
          'hidden': False,
          'locked': True,
          'question': 'What type of attribute is food_cost?'
        }
      ],
      'scored': True,
      'type': 'concept'
    },
    {
      'cases': [
        {
          'code': r"""
          >>> Ant.food_cost
          40031e7755cbca1da159a160d30dbc21
          # locked
          >>> HarvesterAnt.food_cost
          1218df75a941ebc08cec539b1f16208f
          # locked
          >>> ThrowerAnt.food_cost
          e22b4783782de9e5b17a082cf33c6f51
          # locked
          """,
          'hidden': False,
          'locked': True
        },
        {
          'code': r"""
          >>> # Testing HarvesterAnt action
          >>> # Note that initializing an Ant here doesn't cost food, only
          >>> # deploying an Ant in the game simulation does
          >>> #
          >>> # Create a test layout where the colony is a single row with 9 tiles
          >>> beehive = Hive(make_test_assault_plan())
          >>> colony = AntColony(None, beehive, ant_types(), dry_layout, (1, 9))
          >>> #
          >>> colony.food = 4
          >>> harvester = HarvesterAnt()
          >>> harvester.action(colony)
          >>> colony.food
          4c973153c4739175edf72f69c49c509d
          # locked
          >>> harvester.action(colony)
          >>> colony.food
          1417936d6fc6e9b3ac66b50e5d407ada
          # locked
          """,
          'hidden': False,
          'locked': True
        }
      ],
      'scored': True,
      'setup': r"""
      >>> from ants import *
      """,
      'teardown': '',
      'type': 'doctest'
    }
  ]
}
