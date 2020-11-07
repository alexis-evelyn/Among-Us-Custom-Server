package me.alexisevelyn.annotationprocessor;

import me.alexisevelyn.annotationprocessor.annotations.TranslatableText;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main extends AbstractProcessor {
//	private Types typeUtils;
//	private Elements elementUtils;
//	private Filer filer;
//	private Messager messager;

	// http://hannesdorfmann.com/annotation-processing/annotationprocessing101
	public static void main(String... args) {
		System.out.println("Please Do Not Run This Directly!!!");
	}

//	@Override
//	public synchronized void init(ProcessingEnvironment env) {
//		super.init(env);
//
//		typeUtils = processingEnv.getTypeUtils();
//		elementUtils = processingEnv.getElementUtils();
//		filer = processingEnv.getFiler();
//		messager = processingEnv.getMessager();
//	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		for (TypeElement annotation : annotations) {
			for (Element element : env.getElementsAnnotatedWith(annotation)) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found @TranslatableText", element);
			}
		}

		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new LinkedHashSet<>();
		annotations.add(TranslatableText.class.getCanonicalName());

		return annotations;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}
