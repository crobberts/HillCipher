
# Declare any extra files needed to compile HillCipher here:
HILLCIPHEREXTRAS=
# Declare any extra files needed to compile HillKeys here:
HILLKEYSEXTRAS=
# Declare any extra files needed to compile HillDecipher here:
HILLDECIPHEREXTRAS=

hill1.zip: HillCipher.java $(HILLCIPHEREXTRAS)
	zip $@ $^

hill2.zip: HillDecipher.java $(HILLDECIPHEREXTRAS) HillKeys.java $(HILLKEYSEXTRAS) HillCipher.java $(HILLCIPHEREXTRAS)
	zip $@ $^

hill3.zip: HillDecipher.java $(HILLDECIPHEREXTRAS) HillKeys.java $(HILLKEYSEXTRAS) HillCipher.java $(HILLCIPHEREXTRAS)
	zip $@ $^
