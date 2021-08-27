test = {
  'name': 'Problem 0',
  'points': 0,
  'suites': [
    {
      'cases': [
        {
          'answer': '70e772ab89da6efa9faf6e7d3dfb0985',
          'choices': [
            r"""
            It represents armor protecting the insect, so the insect can only
            be damaged when its armor reaches 0
            """,
            r"""
            It represents the strength of an insect against attacks, which
            doesn't change throughout the game
            """,
            r"""
            It represents the amount of health the insect has left, so the
            insect is eliminated when it reaches 0
            """
          ],
          'hidden': False,
          'locked': True,
          'question': r"""
          What is the significance of an Insect's armor attribute? Does this
          value change? If so, how?
          """
        },
        {
          'answer': '48b3cdb37b6261681ab292496acfb6c8',
          'choices': [
            'damage',
            'armor',
            'place',
            'bees'
          ],
          'hidden': False,
          'locked': True,
          'question': 'Which of the following is a class attribute of the Insect class?'
        },
        {
          'answer': '151b8d640486db7ae481ea363bfeba27',
          'choices': [
            'instance, each Ant instance needs its own armor value',
            'instance, each Ant starts out with a different amount of armor',
            'class, Ants of the same subclass all have the same amount of starting armor',
            'class, when one Ant gets damaged, all ants receive the same amount of damage'
          ],
          'hidden': False,
          'locked': True,
          'question': 'Is the armor attribute of the Ant class an instance or class attribute? Why?'
        },
        {
          'answer': 'f5295b4767c62ef6bcc2678487f1e86d',
          'choices': [
            'instance, each Ant does damage to bees at different rates',
            'instance, the damage an Ant depends on where the Ant is',
            'class, all Ants of the same subclass deal the same damage',
            'class, all Ants deal the same damage'
          ],
          'hidden': False,
          'locked': True,
          'question': r"""
          Is the damage attribute of an Ant subclass (such as ThrowerAnt) an
          instance or class attribute? Why?
          """
        },
        {
          'answer': 'df93334c805113354eb6d6a74bcdd61f',
          'choices': [
            'Insect',
            'Place',
            'Bee',
            'Ant'
          ],
          'hidden': False,
          'locked': True,
          'question': 'Which class do both Ant and Bee inherit from?'
        },
        {
          'answer': '4660bbf21d9f45463de1e04aef08c934',
          'choices': [
            r"""
            Ants and Bees both have the attributes armor, damage, and place
            and the methods reduce_armor and action
            """,
            r"""
            Ants and Bees both have the attribute damage and the methods
            reduce_armor and action
            """,
            'Ants and Bees both take the same action each turn',
            'Ants and Bees have nothing in common'
          ],
          'hidden': False,
          'locked': True,
          'question': 'What do instances of Ant and instances of Bee have in common?'
        },
        {
          'answer': 'bc5cd86150b70eb5c32ed1f082b22ecb',
          'choices': [
            'There can be one Ant and many Bees in a single Place',
            'There can be one Bee and many Ants in a single Place',
            'There is no limit on the number of insects of any type in a single Place',
            'Only one insect can be in a single Place at a time'
          ],
          'hidden': False,
          'locked': True,
          'question': r"""
          How many insects can be in a single Place at any given time in the
          game?
          """
        },
        {
          'answer': 'cd289725acf45c0670b47073426d0212',
          'choices': [
            'The bee collects one pollen point for the Bee Hive',
            'The bee flies to the nearest Ant and attacks it',
            'The bee stings the ant in its place or moves to the next place if there is no ant in its place',
            'The bee stings the ant in its place and then moves to the next place'
          ],
          'hidden': False,
          'locked': True,
          'question': 'What does a Bee do during one of its turns?'
        },
        {
          'answer': '83d8d695a622741dff2c58a37665f9d3',
          'choices': [
            'When the bees enter the colony',
            'When the colony runs out of food',
            'When the bees reach the end of the tunnel or when the Queen Ant is killed',
            'When no ants are left on the map'
          ],
          'hidden': False,
          'locked': True,
          'question': 'When is the game lost?'
        }
      ],
      'scored': True,
      'type': 'concept'
    }
  ]
}