//Example usage: Connect to a device by its name and UUID
//const deviceName = "SERGIU";  // Replace with the actual device name
//const serviceUUID = "00001101-0000-1000-8000-00805F9B34FB".replace(/-/g, '');  // Correct hyphenated UUID format
//let socket;  // To store the socket connection
document.addEventListener('DOMContentLoaded', function () {
    const movementElement = document.getElementById('movement');

    // Check if the element is available
    if (!movementElement) {
        console.error('Element with id "movement" not found.');
        return;
    }
    //startTracking(movementElement);
    //sendDataToDevice("30:03:C8:39:0E:58","TEST!");
    tizen.power.request('SCREEN', 'SCREEN_NORMAL');
    connectToDevice("30:03:C8:39:0E:58", function(socket) {
    	startTracking(movementElement,socket);
        
    });
});
function start(){
	console.log('Sensor Started!');
}

function startTracking(movementElement,socket) {
    try {
        // Inform that the accelerometer is being initialized
        movementElement.innerText = 'Initializing accelerometer...';

        // Check if the sensor is available
        var accelerationSensor=tizen.sensorservice.getDefaultSensor('GYROSCOPE');
        accelerationSensor.setChangeListener(function(sensorData) {
        	console.log("Accelerometer data: " + JSON.stringify(sensorData));
            movementElement.innerText = `X: ${sensorData.x.toFixed(3)} Y: ${sensorData.y.toFixed(3)} Z: ${sensorData.z.toFixed(3)}`;
            sendDataToPC(socket, `X: ${sensorData.x}, Y: ${sensorData.y}, Z: ${sensorData.z}`);
        },200);
        accelerationSensor.start(start, null);
        
        
    } catch (err) {
        console.error('Error initializing accelerometer:', err.message);
        movementElement.innerText = `Error: ${err.message}`;
    }
}

//Function to connect and prepare the socket
function connectToDevice(deviceAddress, callback) {
    try {
        console.log("Starting Bluetooth connection...");

        // Ensure Bluetooth is powered on
        const adapter = tizen.bluetooth.getDefaultAdapter();
        if (!adapter.powered) {
            console.error("Bluetooth is not powered on.");
            return;
        }

        console.log("Attempting to find the device...");

        // Find the device by address
        adapter.getDevice(deviceAddress, function (device) {
            console.log("Device found:", device.name);

            // Use the hyphenated RFCOMM UUID (as Tizen expects this format)
            const hyphenatedUUID = "00001101-0000-1000-8000-00805F9B34FB";  // This matches Tizen's expected format

            // Connect to the device using the hyphenated RFCOMM UUID
            device.connectToServiceByUUID(hyphenatedUUID, function (socket) {
                console.log("Connected to the device!");

                // Pass the socket to the callback function
                callback(socket);

            }, function (error) {
                console.error("Failed to connect to the service:", error.message);
            });
        }, function (error) {
            console.error("Failed to find device:", error.message);
        });
    } catch (e) {
        console.error("Error occurred:", e.message);
    }
}

// Function to send data
function sendDataToPC(socket, data) {
    try {
        // Ensure that 'data' is an array (you may need to pass a normal array of bytes)
        let byteArray;
        if (Array.isArray(data)) {
            byteArray = data;
        } else {
            // If it's a string, convert it into a byte array (using char code)
            byteArray = Array.from(new TextEncoder().encode(data));
        }

        console.log("Data to send:", byteArray);

        // Write data to the socket
        var length = socket.writeData(byteArray);
        console.log("Data sent successfully, length:", length);
  
    } catch (e) {
        console.error("Error occurred while sending data:", e.message);
    }
}
