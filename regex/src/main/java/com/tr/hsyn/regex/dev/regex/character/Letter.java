package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.cast.Quanta;
import com.tr.hsyn.regex.cast.Text;


public interface Letter extends Character {
	
	Text ZERO_OR_ONE  = Text.of(Character.LETTER + Quanta.ZERO_OR_ONE);
	Text ZERO_OR_MORE = Text.of(Character.LETTER + Quanta.ZERO_OR_MORE);
	Text ONE_OR_MORE  = Text.of(Character.LETTER + Quanta.ONE_OR_MORE);
}
