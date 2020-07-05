package part2afp

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)
  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1

  List(1, 2, 3).map(x => inc(x)) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curriedAddMethod(x: Int)(y: Int) = x + y


  val add7_11: Int => Int = x => simpleAddFunction(7, x)
  val add7_12: Int => Int = x => simpleAddMethod(x, 7)
  val add7_13: Int => Int = curriedAdder(7)

  val add7 = (x: Int) => simpleAddFunction(7, x) // simplest
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int) // works as well

  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF = alternative syntax

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
  // y => simpleAddMethod(7, y)


  // EXERCISES
  /*
    1.  Process a list of numbers and return their string representations with different formats
        Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */
  def mycurriedFormmater(formatter: String, value: Double) = formatter.format(value)

  val format4_2 = mycurriedFormmater("%4.2f", _: Double)
  val format8_6 = mycurriedFormmater("%8.6f", _: Double)
  val format14_12 = mycurriedFormmater("%14.12f", _: Double)

  List(1.2, 2.2342342342, 4.22342342).foreach(x => {
    println(format4_2(x))
    println(format8_6(x))
    println(format14_12(x))
  })

  def curriedFormatter(s: String)(number: Double): String = s.format(number)

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(curriedFormatter("%14.12f"))) // compiler does sweet eta-expansion for us

  /*
    2.  difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1

  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42

  def parenMethod(): Int = 42

  /*
    calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */
  byName(23) // ok
  byName(method) // ok
  byName(parenMethod())
  byName(parenMethod) // ok but beware ==> byName(parenMethod())
  //  byName(() => 42) // not ok
  byName((() => 42) ()) // ok
  //  byName(parenMethod _) // not ok

  //  byFunction(45) // not ok
  //  byFunction(method) // not ok!!!!!! does not do ETA-expansion!
  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 46) // works
  byFunction(parenMethod _) // also works, but warning- unnecessary

}
