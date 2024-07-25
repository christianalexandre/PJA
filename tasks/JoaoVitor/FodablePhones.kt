fun main()
{
    var phone = FoldablePhone()
    phone.openOrClosed()
    phone.switchOn()
}

open class Phone(var isScreenLightOn: Boolean = false){
    
    open fun switchOn() {
        isScreenLightOn = true
    }

    open fun switchOff() {
        isScreenLightOn = false
    }

    open fun checkPhoneScreenLight() {
        var phoneScreenLight = if (isScreenLightOn) "on" else "off"
        println("The phone screen's light is $phoneScreenLight.")
    }
}

   class FoldablePhone (var close: Boolean = true): Phone() {
    
    fun openOrClosed (){
        if (close == false){
            isScreenLightOn = true
            println(isScreenLightOn)
        }
        else {
            isScreenLightOn = false
            println(isScreenLightOn)
        }
    }
    
    override fun switchOn(){
        if (close == false){
            isScreenLightOn = true
            checkPhoneScreenLight()
            println("Phone opened")
        }
        else {
            isScreenLightOn = false
            checkPhoneScreenLight()
            println("Phone closed")
        }
    }
}