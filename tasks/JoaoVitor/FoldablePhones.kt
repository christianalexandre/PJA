fun main() {
    val foldablePhone = FoldablePhone(isScreenOpen = false, isScreenClose = true)
    
    foldablePhone.checkPhoneScreenLight()
    foldablePhone.switchOn()
    foldablePhone.checkPhoneScreenLight()
}

open class Phone(var isScreenLightOn: Boolean = false){
    open fun switchOn() {
        isScreenLightOn = true
    }
    
    open fun switchOff() {
        isScreenLightOn = false
    }
    
    fun checkPhoneScreenLight() {
        val phoneScreenLight = if (isScreenLightOn) "on" else "off"
        println("The phone screen's light is $phoneScreenLight.")
    }
}

class FoldablePhone(val isScreenOpen: Boolean, val isScreenClose: Boolean): Phone(isScreenLightOn = isScreenOpen) {
	
    override fun switchOn() {
        if (isScreenClose) {
            super.switchOn()
        }else{
            println("error")
        } 
    }
}