package me.alexisevelyn.annotationprocessor;

import me.alexisevelyn.annotationprocessor.annotations.TranslatableText;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main extends AbstractProcessor {
	// private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;
	// private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<String, FactoryGroupedClasses>();

	// http://hannesdorfmann.com/annotation-processing/annotationprocessing101
	public static void main(String... args) {
		System.out.println("Please Do Not Run This Directly!!!");
	}

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(processingEnv);

		// typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
		filer = processingEnv.getFiler();
		messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
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
