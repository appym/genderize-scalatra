import com.scalatra.test._
import org.scalatra._
import javax.servlet.ServletContext
import com.scalatra.test.SlickSupport

class ScalatraBootstrap extends LifeCycle with SlickSupport {
  override def init(context: ServletContext) {
    configureDB()
    context.mount(new GenderController, "/*")
  }

  override def destroy(context: ServletContext) {
    super.destroy(context)

  }
}
