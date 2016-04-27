package org.kite9.tool.model;

import java.io.IOException;

import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.AnnotatedElementHandle;
import org.kite9.framework.model.AnnotationHandle;
import org.kite9.framework.model.ClassHandle;
import org.kite9.framework.model.ConstructorHandle;
import org.kite9.framework.model.FieldHandle;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.PackageHandle;
import org.kite9.framework.model.ProjectModelImpl;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.asm.commons.EmptyVisitor;
import org.springframework.core.io.Resource;

/**
 * Handles visiting class file resources and adding the details to the model.
 * 
 * @author robmoffat
 * 
 */
public class ClassFileModelBuilder {

	ProjectModelImpl model = new ProjectModelImpl();

	public ProjectModelImpl getModel() {
		return model;
	}

	public static final EmptyVisitor EMPTY_VISITOR = new EmptyVisitor();

	public void visit(Resource resource) throws IOException {
		ClassReader cr = new ClassReader(resource.getInputStream());
		cr.accept(createClassVisitor(model), false);
	}

	protected String convertAnnotationDescriptor(String desc) {
		Type t = Type.getType(desc);
		return t.getInternalName();
	}

	public ClassVisitor createClassVisitor(final ProjectModelImpl model) {
		return new ClassVisitor() {

			String className;

			public void visit(int version, int access, String name, String sig, String superName, String[] interfaces) {
				this.className = name;
				model.addSubclass(superName, name);

				String packageName = getPackageName(name);
				model.addPackageClass(packageName, name);
				model.addClass(name);

				for (int j = 0; j < interfaces.length; j++) {
					model.addSubclass(interfaces[j], name);
					addDependency(name, model, interfaces[j]);
				}
				
				addDependency(name, model, superName);
			}

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				model.addClassAnnotation(convertAnnotationDescriptor(desc), className);
				addDependency(className, model, desc, false);
				return createAnnotationVisitor(model, new ClassHandle(className), desc);
			}

			public FieldVisitor visitField(int access, String name, String desc, String sign, Object value) {
				final FieldHandle mh = new FieldHandle(className, name);
				addDependency(className, model, desc, true);
				return createFieldVisitor(model, className, mh);
			}

			public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] exceptions) {
				final MemberHandle mh = createHandle(className, name, desc);
				addDependency(className, model, desc, true);
				return createMethodVisitor(model, className, mh);

			}

			public void visitOuterClass(String arg0, String arg1, String arg2) {
			}

			public void visitSource(String arg0, String arg1) {
			}

			public void visitAttribute(Attribute arg0) {
			}

			public void visitEnd() {
			}

			public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
			}
		};
	}

	private FieldVisitor createFieldVisitor(final ProjectModelImpl model, final String className, final FieldHandle mh) {
		return new FieldVisitor() {

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				model.addMemberAnnotation(convertAnnotationDescriptor(desc), mh);
				return createAnnotationVisitor(model, mh, desc);
			}

			public void visitAttribute(Attribute arg0) {
			}

			public void visitEnd() {
			}

		};
	}

	private AnnotationVisitor createAnnotationVisitor(final ProjectModelImpl model,
			final AnnotatedElementHandle<?> handle, final String desc) {
		return new AnnotationVisitor() {

			String field = null;

			public void visit(String arg0, Object arg1) {
				setFieldName(arg0);
				if (arg1 instanceof Type) {
					Type referenced = (Type) arg1;
					String name = referenced.getClassName();
					Type annotationClass = Type.getType(desc);
					AnnotationHandle ah = new AnnotationHandle(field, handle, MemberHandle
							.convertClassName(annotationClass.getClassName()));
					model.addAnnotationReference(MemberHandle.convertClassName(name), ah);
					addDependency(handle.getDeclaringClass(), model, referenced);
				}
			}

			public AnnotationVisitor visitAnnotation(String arg0, String arg1) {
				return EMPTY_VISITOR;
			}

			public AnnotationVisitor visitArray(final String arg0) {
				setFieldName(arg0);
				return this;
			}

			private void setFieldName(final String arg0) {
				if (arg0 != null)
					field = arg0;
			}

			public void visitEnd() {
			}

			public void visitEnum(String arg0, String arg1, String arg2) {
			}

		};
	}

	private MethodVisitor createMethodVisitor(final ProjectModelImpl model, final String className,
			final MemberHandle mh) {
		return new MethodVisitor() {

			public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
				model.addMemberAnnotation(convertAnnotationDescriptor(desc), mh);
				return createAnnotationVisitor(model, mh, desc);
			}

			public void visitFieldInsn(int arg0, String owner, String name, String desc) {
				FieldHandle field = new FieldHandle(owner, name);
				model.addCalls(mh, field);
				addDependency(className, model, desc, true);
				model.addClassDependency(className, owner);
			}

			public void visitMethodInsn(int arg0, String owner, String name, String desc) {
				MemberHandle remoteMethod = createHandle(owner, name, desc);
				model.addCalls(mh, remoteMethod);
				addDependency(className, model, desc, true);
				model.addClassDependency(className, owner);
			}

			/* Consider implementing next 2 */
			public void visitTypeInsn(int arg0, String type) {
				model.addClassDependency(className, type);
			}

			public void visitMultiANewArrayInsn(String desc, int arg1) {
				addDependency(className, model, desc, false);
			}

			/**
			 * TODO: add support for parameter annotations
			 */
			public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
				return EMPTY_VISITOR;
			}

			public void visitVarInsn(int arg0, int arg1) {
			}

			public AnnotationVisitor visitAnnotationDefault() {
				return EMPTY_VISITOR;
			}

			public void visitAttribute(Attribute arg0) {
			}

			public void visitCode() {
			}

			public void visitEnd() {
			}

			public void visitIincInsn(int arg0, int arg1) {
			}

			public void visitInsn(int arg0) {
			}

			public void visitIntInsn(int arg0, int arg1) {
			}

			public void visitJumpInsn(int arg0, Label arg1) {
			}

			public void visitLabel(Label arg0) {
			}

			public void visitLdcInsn(Object arg0) {
			}

			public void visitLineNumber(int arg0, Label arg1) {
			}

			public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {
			}

			public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
			}

			public void visitMaxs(int arg0, int arg1) {
			}

			public void visitTableSwitchInsn(int arg0, int arg1, Label arg2, Label[] arg3) {
			}

			public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
			}

		};
	}

	private void addDependency(final String className, final ProjectModelImpl model, String desc, boolean returnType) {
			Type t = returnType ? Type.getReturnType(desc) : Type.getType(desc);
			addDependency(className, model, t);
	}
	
	private void addDependency(final String className, final ProjectModelImpl model, Type t) {
		try {
			if (t.getSort() == Type.ARRAY) {
				t = t.getElementType();
			}

			if (t.getSort() == Type.OBJECT) {
				String className2 = t.getInternalName();
				addDependency(className, model, className2);
			}
		} catch (RuntimeException e) {
			throw new Kite9ProcessingException("Could not handle type: " + t.getDescriptor(), e);
		}
	}

	private void addDependency(final String className, final ProjectModelImpl model, String className2) {
		model.addClassDependency(className, className2);
		String packageName1 = getPackageName(className);
		String packageName2 = getPackageName(className2);
		PackageHandle ph1 = new PackageHandle(packageName1, className);
		PackageHandle ph2 = new PackageHandle(packageName2, className2);
		
		model.addPackageDependency(ph1, ph2);
	}

	private String getPackageName(String name) {
		int li = name.lastIndexOf("/");
		li = li==-1 ? 0 : li;
		return name.substring(0, li);
	}
	
	private MemberHandle createHandle(String owner, String name, String desc) {
		if (name.equals("<init>")) {
			return new ConstructorHandle(owner, desc);			
		} else {
			return new MethodHandle(owner, name, desc);
		}
	}

}
