package com.tr.hsyn.warnerbros;

import com.google.auto.service.AutoService;
import com.tr.hsyn.warnerlabel.Remember;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({ "com.tr.hsyn.warnerlabel.Remember" })
@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_20)
@AutoService(Processor.class)
public class WarnTurner extends AbstractProcessor {
	
	protected Messager messager;
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		
		super.init(processingEnv);
		
		messager = processingEnv.getMessager();
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, @Nonnull RoundEnvironment roundEnv) {
		
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Remember.class);
		
		if (elements.isEmpty()) return true;
		
		elements.forEach(this::inspectAnnotation);
		
		return true;
	}
	
	private void inspectAnnotation(@Nonnull Element element) {
		
		Remember annotation = element.getAnnotation(Remember.class);
		
		Name   name   = element.getSimpleName();
		String note   = annotation.note();
		String time   = annotation.time();
		String writer = annotation.writer();
		
		Element enclosed = element.getEnclosingElement();
		
		// noinspection StringBufferReplaceableByString
		StringBuilder sb = new StringBuilder();
		
		sb.append("Remember ")
			.append("\"")
			.append(name)
			.append("\"")
			.append(" in the <")
			.append(enclosed)
			.append("> [")
			.append("note=")
			.append(note.isEmpty() ? "-" : note).append(", ")
			.append("time=")
			.append(time.isEmpty() ? "-" : time).append(", ")
			.append("writer=")
			.append(writer.isEmpty() ? "-" : writer)
			.append("]");
		
		log(sb.toString());
		
	}
	
	private void log(Object message, Object... args) {
		
		messager.printMessage(Diagnostic.Kind.WARNING, String.format(message.toString(), args));
	}
}