package zz.utils.references;

/**
 * Reference to a property listener. Implementations can be weak or hard references.
 */
public interface IRef<T>
{
	public T get();
}