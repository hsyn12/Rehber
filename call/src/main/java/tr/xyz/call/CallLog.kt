package tr.xyz.call

class CallLog(val calls: List<Call>) {
	
	operator fun contains(call: Call) = calls.contains(call)
}