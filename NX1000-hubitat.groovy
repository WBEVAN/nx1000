/**
 *  Nexia NX1000 One Touch Controller
 *  IMPORT URL: https://raw.githubusercontent.com/WBEVAN/nx1000/master/NX1000-hubitat.groovy
 *
 *  This driver implements the NX1000 as a button controller.  It has been built on the great
 *  work of others.  Like Wayne Pirtle from which this is forked I am humbled by their talent,
 *  and priviledged to use portions of thier work. This has been an great learing curve as this
 *  is my first dive into writing a driver for the Hubitat
 *
 *  THANKS TO....
 *
 *  Wayne Pirtle for getting the driver in a good starting shape and the cast before him.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  WHATS NEW in 2.0
 *
 *  - Added in 3 individual commands that now allow setting individual buttons on each panel
 *  - Driver now supports selecting from 'Small' or 'Large' fonts and this can be done on an individual button
 *  - Button Text preferences can now be used to indicate to use Large or small font, append :L for large or :S to the text
 *  - Panel commands can be used directly from the likes of a rule to set update button text based off an action. (Only works if the device is awake)
 *  - Label update for a button contains two passes. One to clear it (Passes spaces) and then another with the updated text
 *  - Use more intuitve enumeration for the various commands
 *
 *  Changelog:
 *
 *  1.0 (07 Jan 2020) - Initial release
 *  1.1 (07 Jan 2020) - Added "Enable Debug Logging" control to all log.debug commands.
 *                      Removed some debug commands that were commented out.
 *  1.2 (09 Jan 2020) - Removed the button specific commands.  They were not fully implemented.
 *			Removed the Double Tap command.  The controller doesn't provide a double tap capability.
 *			Corrected the trim lengh of labels to a maximum of 12 characters.  It had been 14.
 *  2.0 (29 Feb 2020) - Forked from Wayne Pirtle by Wayne Bevan
 *
 *
*/

 metadata {

  definition (name: "Nexia NX1000 One Touch Controller", namespace: "bevan", author: "Wayne Bevan") {
        capability "PushableButton"
        capability "HoldableButton"
        capability "ReleasableButton"

        capability "Battery"


        capability "Configuration"

        command "push", [ [name:"ButtonNumber",type: "ENUM", description: "Button", constraints: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]]]
        command "hold", [ [name:"ButtonNumber",type: "ENUM", description: "Button", constraints: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]]]
        command "release", [ [name:"ButtonNumber",type: "ENUM", description: "Button", constraints: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]]]


       // Seperate Commands for each of the 3 panels
        command "SetButtonTextPanel1", [
            [name:"ButtonNumber", type: "ENUM", description: "Button", constraints: [1,2,3,4,5]],
            [name:"TextType", type: "ENUM", description: "Text", constraints: ["Small","Large"]],
            [name:"ButtonText", type: "STRING"]
            ]

        command "SetButtonTextPanel2", [
            [name:"ButtonNumber", type: "ENUM", description: "Button", constraints: [6,7,8,9,10]],
            [name:"TextType", type: "ENUM", description: "Text", constraints: ["Small","Large"]],
            [name:"ButtonText", type: "STRING"]
            ]

        command "SetButtonTextPanel3", [
            [name:"ButtonNumber", type: "ENUM", description: "Button", constraints: [11,12,13,14,15]],
            [name:"TextType", type: "ENUM", description: "Text", constraints: ["Small","Large"]],
            [name:"ButtonText", type: "STRING"]
            ]

        fingerprint deviceId: "0x1801", inClusters: "0x5E, 0x85, 0x59, 0x80, 0x5B, 0x70, 0x5A, 0x7A, 0x72, 0x8F, 0x73, 0x2D, 0x93, 0x92, 0x86, 0x84"
  }



  preferences {

        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true

        // Button Text is used to indicate if ti is Small ':S' or 'or large :L', if manually entering these should be on the end of the button text
  	    input "buttonLabel1",  "string", title: "Button 1 Label",  defaultValue: "Button 1:S",  displayDuringSetup: true, required: false
        input "buttonLabel2",  "string", title: "Button 2 Label",  defaultValue: "Button 2:S",  displayDuringSetup: true, required: false
        input "buttonLabel3",  "string", title: "Button 3 Label",  defaultValue: "Button 3:S",  displayDuringSetup: true, required: false
        input "buttonLabel4",  "string", title: "Button 4 Label",  defaultValue: "Button 4:S",  displayDuringSetup: true, required: false
        input "buttonLabel5",  "string", title: "Button 5 Label",  defaultValue: "Button 5:S",  displayDuringSetup: true, required: false
		input "buttonLabel6",  "string", title: "Button 6 Label",  defaultValue: "Button 6:S",  displayDuringSetup: true, required: false
        input "buttonLabel7",  "string", title: "Button 7 Label",  defaultValue: "Button 7:S",  displayDuringSetup: true, required: false
        input "buttonLabel8",  "string", title: "Button 8 Label",  defaultValue: "Button 8:S",  displayDuringSetup: true, required: false
        input "buttonLabel9",  "string", title: "Button 9 Label",  defaultValue: "Button 9:S",  displayDuringSetup: true, required: false
        input "buttonLabel10", "string", title: "Button 10 Label", defaultValue: "Button 10:S", displayDuringSetup: true, required: false
		input "buttonLabel11", "string", title: "Button 11 Label", defaultValue: "Button 11:S", displayDuringSetup: true, required: false
        input "buttonLabel12", "string", title: "Button 12 Label", defaultValue: "Button 12:S", displayDuringSetup: true, required: false
        input "buttonLabel13", "string", title: "Button 13 Label", defaultValue: "Button 13:S", displayDuringSetup: true, required: false
        input "buttonLabel14", "string", title: "Button 14 Label", defaultValue: "Button 14:S", displayDuringSetup: true, required: false
        input "buttonLabel15", "string", title: "Button 15 Label", defaultValue: "Button 15:S", displayDuringSetup: true, required: false



  }


  tiles (scale: 2) {


    	valueTile("ok", "device.buttonNum", width: 2, height: 2) {
			state("", label:'${currentValue}',
				backgroundColor: "#79b821"
			)
		}


    main "ok"
    details (["ok", "battery1", "configure", "btn1", "btn2", "btn3","btn4","btn5","btn6","btn7","btn8","btn9","btn10",
              "btn11","btn12","btn13","btn14","btn15"])
  }
}


def logsOff(){
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable",[value:"false",type:"bool"])
}

def parse(String description) {
    if (logEnable) log.debug "parse description: ${description}"
    //def cmd = zwave.parse(description,[ 0x26: 1])
    // For debug dont care about the versions....
    def cmd = zwave.parse(description)
    if (logEnable) log.debug "CMD : ${cmd}"

    if (cmd) {zwaveEvent(cmd)}
    return
}

//Added these items
def zwaveEvent(hubitat.zwave.commands.batteryv1.BatteryReport cmd) {
    if (logEnable) log.debug "BatteryReport value: ${cmd}"
}

def zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
    if (logEnable) log.debug "SwitchBinaryReport value: ${cmd}"


}


def SetButtonTextPanel1(btnNumber, String fontType, String text) {
    if (logEnable) log.debug "SetButtonTextPanel1 Btn#: ${btnNumber}, Font: ${fontType} Btn Text: ${text}"
    return configureOneButton(btnNumber.toInteger(), text, fontType)
}

def SetButtonTextPanel2(btnNumber, String fontType, String text) {
    if (logEnable) log.debug "SetButtonTextPanel2 Btn#: ${btnNumber}, Font: ${fontType} Btn Text: ${text}"
    return configureOneButton(btnNumber.toInteger(), text, fontType)
}

def SetButtonTextPanel3(btnNumber, String fontType, String text) {
    if (logEnable) log.debug "SetButtonTextPanel3 Btn#: ${btnNumber}, Font: ${fontType} Btn Text: ${text}"
    return configgureOneButton(btnNumber.toInteger(), text, fontType)
}
///-------

//returns on physical
def zwaveEvent(hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
    if (logEnable) log.debug "SwitchMultilevelReport value: ${cmd.value}"
    dimmerEvents(cmd.value,"physical")
}

//returns on digital
def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd) {
    if (logEnable) log.info "BasicReport value: ${cmd.value}"
    dimmerEvents(cmd.value,"digital")
}


def zwaveEvent(hubitat.zwave.commands.centralscenev1.CentralSceneNotification cmd){
    if (logEnable) log.debug "CentralSceneNotification: ${cmd}"

      def button = cmd.sceneNumber
    def key = cmd.keyAttributes
    def action
    switch (key){
        case 0: //pushed
            if (logEnable) log.debug "NEXIA: Key ${button} Text: ${state.buttonLabels[button-1]} - PUSHED"
            action = "pushed"
            break
        case 1:	//released, only after 2
            if (logEnable) log.debug "NEXIA: Key ${button} Text: ${state.buttonLabels[button-1]} - RELEASED"
            state."${button}" = 0
            action = "released"
            break
        case 2:	//holding
            if (state."${button}" == 0){
                state."${button}" = 1
                runInMillis(200,delayHold,[data:button])
                if (logEnable) log.debug "NEXIA: Key ${button} Text: ${state.buttonLabels[button-1]} - HOLDING"
            }
            break
        case 3:	//double tap, 4 is tripple tap
            if (logEnable) log.debug "NEXIA: Key ${button} Text: ${state.buttonLabels[button-1]} - DOUBLE TAP"
            action = "doubleTapped"
            break
    }

    if (action){
        def descriptionText = "${device.displayName} button ${button} Text: ${state.buttonLabels[button-1]} was ${action}"
        if (txtEnable) log.info "${descriptionText}"
        sendEvent(name: "${action}", value: "${button}", descriptionText: descriptionText, isStateChange: true, type: "physical")
    }
    return
}


def zwaveEvent(hubitat.zwave.Command cmd) {
    if (logEnable) log.debug "skip: ${cmd}"
}



def delayHold(button){
    def descriptionText = "${device.displayName} button ${button} was held"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "held", value: "${button}", descriptionText: descriptionText, isStateChange: true, type: "physical")
}

def push(String button){
    def descriptionText = "${device.displayName} button ${button} was pushed"
    def intValue = button.toInteger()
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "pushed", value: "${intValue}", descriptionText: descriptionText, isStateChange: true, type: "digital")
}

def hold(String button){
    def descriptionText = "${device.displayName} button ${button} was held"
    def intValue = button.toInteger()
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "held", value: "${intValue}", descriptionText: descriptionText, isStateChange: true, type: "digital")
}

def release(String button){
    def descriptionText = "${device.displayName} button ${button} was released"
    def intValue = button.toInteger()
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "released", value: "${intValue}", descriptionText: descriptionText, isStateChange: true, type: "digital")
}

def doubleTap(button){
    def descriptionText = "${device.displayName} button ${button} was doubleTapped"
    if (txtEnable) log.info "${descriptionText}"
    def intValue = button.toInteger()
    sendEvent(name: "doubleTapped", value: "${intValue}", descriptionText: descriptionText, isStateChange: true, type: "digital")
}

def installed(){
    log.warn "installed..."
    sendEvent(name: "numberOfButtons", value: 2)
    for (i = 1; i <= 2; i++){
        state."${i}" = 0
    }
    sendEvent(name: "level", value: 20)
}

// Update manufacturer information when it is reported
def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
   if (logEnable) log.debug ("manufacturer specific report recieved")
   if (logEnable) log.debug (cmd)
   if (state.manufacturer != cmd.manufacturerName) {
     updateDataValue("manufacturer", cmd.manufacturerName)
   }
     createEvent([:])
}

def zwaveEvent(hubitat.zwave.commands.wakeupv2.WakeUpNotification cmd) {
    if (logEnable) log.debug ("Received Wakeup notification")
    def result = []
    result << createEvent(descriptionText: "${device.displayName} woke up", displayed: false)
	if (!isDuplicateCall(state.lastWokeUp, 1)) {
		state.lastwokeUp = new Date().time
	}
    result << response(zwave.wakeUpV1.wakeUpNoMoreInformation().format())
    return result
}

private isDuplicateCall(lastRun, allowedEverySeconds) {
	def result = false
	if (lastRun) {
		result =((new Date().time) - lastRun) < (allowedEverySeconds * 1000)
	}
	result
}


def setButtonLabels() {

    //Initialize button labels
    if (!state.buttonLabelsInitialized)
    {
       state.buttonLabelsInitialized=true
       state.buttonLabels=["small btn 1:S", "Large 2:L",  "Button 3:S",  "Button 4:S",  "Button 5:S",  "Button 6:S",  "Button 7:S", "Button 8:S",
                           "Button 9:S", "Button 10:S", "Button 11:S", "Button 12:S", "Button 13:S", "Button 14:S", "Button 15:S"]
    }

    // set the button labels based on preferences
    if (buttonLabel1 != null) {
       state.buttonLabels[0]=buttonLabel1
    }
    if (buttonLabel2 != null) {
       state.buttonLabels[1]=buttonLabel2
    }
    if (buttonLabel3 != null) {
       state.buttonLabels[2]=buttonLabel3
    }
    if (buttonLabel4 != null) {
       state.buttonLabels[3]=buttonLabel4
    }
    if (buttonLabel5 != null) {
       state.buttonLabels[4]=buttonLabel5
    }
    if (buttonLabel6 != null) {
       state.buttonLabels[5]=buttonLabel6
    }
    if (buttonLabel7 != null) {
       state.buttonLabels[6]=buttonLabel7
    }
    if (buttonLabel8 != null) {
       state.buttonLabels[7]=buttonLabel8
    }
    if (buttonLabel9 != null) {
       state.buttonLabels[8]=buttonLabel9
    }
    if (buttonLabel7 != null) {
       state.buttonLabels[6]=buttonLabel7
    }
    if (buttonLabel8 != null) {
       state.buttonLabels[7]=buttonLabel8
    }
    if (buttonLabel9 != null) {
       state.buttonLabels[8]=buttonLabel9
    }
    if (buttonLabel10 != null) {
       state.buttonLabels[9]=buttonLabel10
    }
    if (buttonLabel11 != null) {
       state.buttonLabels[10]=buttonLabel11
    }
    if (buttonLabel12 != null) {
       state.buttonLabels[11]=buttonLabel12
    }
    if (buttonLabel13 != null) {
       state.buttonLabels[12]=buttonLabel13
    }
    if (buttonLabel14 != null) {
       state.buttonLabels[13]=buttonLabel14
    }
    if (buttonLabel15 != null) {
       state.buttonLabels[14]=buttonLabel15
    }

    //send the button labels
    sendEvent(name: "label1",  value: state.buttonLabels[0],  isStateChange: true)
    sendEvent(name: "label2",  value: state.buttonLabels[1],  isStateChange: true)
    sendEvent(name: "label3",  value: state.buttonLabels[2],  isStateChange: true)
    sendEvent(name: "label4",  value: state.buttonLabels[3],  isStateChange: true)
    sendEvent(name: "label5",  value: state.buttonLabels[4],  isStateChange: true)
    sendEvent(name: "label6",  value: state.buttonLabels[5],  isStateChange: true)
    sendEvent(name: "label7",  value: state.buttonLabels[6],  isStateChange: true)
    sendEvent(name: "label8",  value: state.buttonLabels[7],  isStateChange: true)
    sendEvent(name: "label9",  value: state.buttonLabels[8],  isStateChange: true)
    sendEvent(name: "label10", value: state.buttonLabels[9],  isStateChange: true)
    sendEvent(name: "label11", value: state.buttonLabels[10], isStateChange: true)
    sendEvent(name: "label12", value: state.buttonLabels[11], isStateChange: true)
    sendEvent(name: "label13", value: state.buttonLabels[12], isStateChange: true)
    sendEvent(name: "label14", value: state.buttonLabels[13], isStateChange: true)
    sendEvent(name: "label15", value: state.buttonLabels[14], isStateChange: true)

    sendEvent(name: "numberOfButtons", value: 15, displayed: false)
}


// Support to configure a single button, fontType is Small or Large
def configureOneButton(Integer buttonNum, String buttonText, String fontType){
  def commands = []

    def intValue = buttonNum
    if (logEnable) log.debug ("Entered congureOneButton")


    // Clear the current text becuase well its nice to do
    commands << createCommandToSetButtonLabel(intValue-1, "                  ",4)

    commands << "delay 1000"
    // Set font accordingly
    if(fontType == "Small"){
        state.buttonLabels[intValue-1]=limitTextLength(buttonText)+":S"
        commands << createCommandToSetButtonLabel(intValue-1, limitTextLength(state.buttonLabels[intValue-1]),0)
    }
    else{
        state.buttonLabels[intValue-1]=limitTextLength(buttonText)+":L"
        commands << createCommandToSetButtonLabel(intValue-1, limitTextLength(state.buttonLabels[intValue-1]),4)
    }
    commands << zwave.batteryV1.batteryGet().format()

    delayBetween(commands,1200)

    // Update the preferences
    device.updateSetting("buttonLabel${intValue}",[value: state.buttonLabels[intValue-1] , type:"string"])

    setButtonLabels()

    return commands
}

// Configure the device button types and corresponding scene numbers
def configurationCmds() {
    def commands = []

    if (logEnable) log.debug ("Entered configurationCmds")

    // Loop through all the buttons on the controller
    for (def buttonNum = 1; buttonNum <= 15; buttonNum++) {
       // set a unique corresponding scene for each button
       commands << zwave.sceneControllerConfV1.sceneControllerConfSet(groupId: buttonNum, sceneId: buttonNum).format()
       // set configuration for each button to zero (scene control momontary)
       // 0 is moentary
       // 2 is toggle - Cant seem to get this to work. No events generated.

       commands << zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: buttonNum + 1, size: 1).format()
    }

    commands << zwave.batteryV1.batteryGet().format()
//    commands << associateHub()

    setButtonLabels()

    delayBetween(commands)

    return commands
}

// Configure the device
def configure() {
    def cmd=configurationCmds()
    //cmd << "delay 100"

    // C&E T: set button texts on device
    for (def buttonNum = 1; buttonNum <= 15; buttonNum++) {

        if (logEnable) log.debug ("Font for button ${buttonNum}, Text = ${state.buttonLabels[buttonNum-1]} is ${getFont(state.buttonLabels[buttonNum-1])}")
        cmd << createCommandToSetButtonLabel(buttonNum-1, "                  ",4)
        cmd << "delay 1200" // using such a long delay seems to be the safest way to go
        cmd << createCommandToSetButtonLabel(buttonNum-1, state.buttonLabels[buttonNum-1], getFont(state.buttonLabels[buttonNum-1]))
        cmd << "delay 1200" // using such a long delay seems to be the safest way to go
    }

    if (logEnable) log.debug("Sending configuration: ${cmd}")
    return cmd
}


// Creates a Screen Meta Data Report command for one button label
//   lineNumber = the number of the line 0..14
//   text = the text to put on the button
// Some default settings are applied (clear line, ASCII+OEM chars, charPos=0)
// Fint default to be large font
def createCommandToSetButtonLabel(lineNumber, text, font = 4) {
	def command = ""

    if (null == text) {
    	log.error "createCommandToSetButtonLabel: text == null"
    }
    else if (lineNumber < 0) {
    	log.error "createCommandToSetButtonLabel: lineNumber < 0"
    }
    else if (lineNumber > 14) {
    	log.error "createCommandToSetButtonLabel: lineNumber > 14"
    }
    else {
        // Command structure - see http://z-wave.sigmadesigns.com/wp-content/uploads/2016/08/SDS12652-13-Z-Wave-Command-Class-Specification-N-Z.pdf
        //   "92" // COMMAND_CLASS_SCREEN_MD
        //   "02" // SCREEN_MD_REPORT
        //   "3B" // MoreData=0, Reserved=0, ScreenSettings=7 (3 bit, 7=keep current content), CharacterEncoding=1 (3 bit, ASCII+OEM)
        //   "1"  // LineSettings=0 (3 bit, 0=selected font), Clear=1 (1 bit, 1=clear line)
        //   "X"  // LineNumber (hex, 4 bit)
        //   "00" // CharacterPosition (hex 8 bit)
        //   "XX" // NumberOfCharacters (hex 8 bit)
        //   "XX" // Character (hex ASCII)
        //   "XX" // Character (hex ASCII)
        // NOTE: Theoretically you can send more than one line in one screenMdReport command.
        //   Hoever, the zwave documentation says that the size of the payload SHOULD NOT be bigger than 48 bytes.
        //   That basically limits us to about 2 words at once.
        //   So, it's easier to send one button label at a time.

        def screenReportHeader = "92"+"02"+"3B"


        def lineSettings = "${font}"  // 0 is small text , 2 is inverse, 4 is Big Text
        def lineNumberString = Integer.toHexString(lineNumber)
        def characterPositionString = "00"
        text = limitTextLength(text)
        def label = asciiToHex(text)
        def numOfChars = label.length()/2 // after conversion to hex each character is represented by two characters
        def numOfCharString = Integer.toHexString(numOfChars.intValue())
        // numOfCharString could have one char only but we need 2
        if (numOfCharString.length() == 1) {
            numOfCharString = "0" + numOfCharString
        }
        command = screenReportHeader + lineSettings + lineNumberString + characterPositionString + numOfCharString + label
    }

    return command
}



private static String asciiToHex(String asciiValue)
{
    char[] chars = asciiValue.toCharArray();
    StringBuffer hex = new StringBuffer();
    for (int i = 0; i < chars.length; i++)
    {
        hex.append(Integer.toHexString((int) chars[i]));
    }
    return hex.toString();
}


def limitTextLength(String text){
        // More than 12 characters on a line in this device isn't practicle. .

        btnStr = text.split(':');
        newText = btnStr[0]
        if (newText.length() > 12) {
            newText = newText.substring(0,12)
        }
        return newText
}

// 0 is small, 4 is large. Default is Large if :S or :L is not specified
def getFont(String text)
{
   String[] str;
   str = text.split(':')
   if(str.length == 1)
        return 4;

   if(str[1] == 'L' )
        return 4;
   else
       return 0;

}
