package reproduce

import com.wix.accord.dsl._
import com.wix.accord.{Validator, validate}
import org.junit.{Assert, Test}

class ReproduceTest {

  @Test
  def aTest(): Unit = {
    val obj = AModel(Seq("123"))
    Assert.assertTrue(validate(obj)(AModel.getValidator).isFailure)
  }
}

case class AModel(anArray: Seq[String])
object AModel {
  implicit val getValidator = validator[AModel] { m =>
    // This doesn't fail
    //(m.anArray.each is notEmpty) and (m.anArray.each has size <= 2)

    //Causes java.lang.IllegalArgumentException: Cannot combine description 'SelfReference' with 'Indexed(0,Empty)'
    m.anArray.each is nonEmptyWithMaxLength(2)
  }

  def nonEmptyWithMaxLength(maxLength: Int): Validator[String] = validator[String] { s =>
    (s is notEmpty) and (s has size <= maxLength)
  }
}
