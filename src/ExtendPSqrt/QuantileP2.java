package ExtendPSqrt;

import java.util.Arrays;

/**
 *
 * 分位值运算基于以下论文：
 * <ul>
 * <li>
 * 1. <a href="http://pierrechainais.ec-lille.fr/Centrale/Option_DAD/IMPACT_files/Dynamic%20quantiles%20calcultation%20-%20P2%20Algorythm.pdf">
 * Raj Jain, Imrich Chlamtac: The P2 Algorithm for Dynamic Calculation of Quantiles and Histograms Without Storing Observations, ACM 28, 10 (1985)
 * </a>
 * </li>
 * <li>
 * 2. <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.50.6060&rep=rep1&type=pdf">
 * Kimmo Raatikainen: Simultaneous estimation of several percentiles, Simulations Councils (1987)
 * </a>
 * </li>
 * </ul>
 *
 * @author BurningIce
 *
 */
public class QuantileP2 {
	private int count;
	private double[] p2_q;
	private int[] p2_n;
	private double[] p2_n_increment;

	public QuantileP2(double[] quantiles) {
		Arrays.sort(quantiles);
		p2_n_increment = new double[quantiles.length * 2 + 3];

		// ~ is the mean of the neighbors, q is quantiles, we need:
		// p2_n_increment = [0, ~, q[0], ~, q[1], ~, q[2], ~, 1]

		// first, fill in all except for the mean
		p2_n_increment[0] = 0.0;
		for (int i = 0; i < quantiles.length; ++i) {
			assert quantiles[i] > 0.0 && quantiles[i] < 1.0;
			// first quantile at 2, one value inbetween:
			p2_n_increment[i * 2 + 2] = quantiles[i];
		}
		p2_n_increment[p2_n_increment.length - 1] = 1.0;

		// then, fill in the values inbetween
		for (int i = 1; i < p2_n_increment.length - 1; i += 2) {
			p2_n_increment[i] = (p2_n_increment[i - 1] + p2_n_increment[i + 1]) / 2;
		}

		// init markers
		p2_n = new int[p2_n_increment.length];
		p2_q = new double[p2_n_increment.length];
		for (int i = 0; i < p2_n.length; ++i) {
			// should this look at the desired marker pos?
			p2_n[i] = i;
		}
	}

	/**
	 * 使用q-markers及样本数量创建P2对象（仅用于2个或多个P2对象合并）
	 * @param p2_q q-markers
	 * @param count 样本数量
	 */
	public QuantileP2(double[] quantiles, double[] p2_q, int count) {
		assert quantiles != null && quantiles.length > 0;
		assert p2_q != null && p2_q.length > 0;
		assert p2_q.length == 2 * quantiles.length + 3;

		this.p2_q = p2_q;
		this.count = count;

		Arrays.sort(quantiles);
		p2_n_increment = new double[quantiles.length * 2 + 3];

		// ~ is the mean of the neighbors, q is quantiles, we need:
		// p2_n_increment = [0, ~, q[0], ~, q[1], ~, q[2], ~, 1]

		// first, fill in all except for the mean
		p2_n_increment[0] = 0.0;
		for (int i = 0; i < quantiles.length; ++i) {
			assert quantiles[i] > 0.0 && quantiles[i] < 1.0;
			// first quantile at 2, one value inbetween:
			p2_n_increment[i * 2 + 2] = quantiles[i];
		}
		p2_n_increment[p2_n_increment.length - 1] = 1.0;

		// then, fill in the values inbetween
		for (int i = 1; i < p2_n_increment.length - 1; i += 2) {
			p2_n_increment[i] = (p2_n_increment[i - 1] + p2_n_increment[i + 1]) / 2;
		}

		// init markers
		p2_n = new int[p2_n_increment.length];
		/// TODO p2_n 使用P2算法中期望值n'(即n_) 初始化？
	}

	public void add(double v) {
		if(Double.isNaN(v)) {
			return;
		}

		int obsIdx = this.count;
		++this.count;

		if (obsIdx < p2_q.length) {
			// initialization
			p2_q[obsIdx] = v;
			if (obsIdx == p2_q.length - 1) {
				// finish initialization
				Arrays.sort(p2_q);
			}
		} else {
			// usual case
			int k = Arrays.binarySearch(p2_q, v);
			if (k < 0) {
				k = -(k + 1);
			}

			if (k == 0) {
				p2_q[0] = v;
				k = 1;
			} else if (k == p2_q.length) {
				k = p2_q.length - 1;
				p2_q[k] = v;
			}

			for (int i = k; i < p2_n.length; ++i) {
				++p2_n[i];
			}

			for (int i = 1; i < p2_q.length - 1; ++i) {
				double n_ = p2_n_increment[i] * obsIdx;
				double di = n_ - p2_n[i];
				if ((di >= 1.0 && p2_n[i + 1] - p2_n[i] > 1)
						|| ((di <= -1.0 && p2_n[i - 1] - p2_n[i] < -1))) {
					int d = di < 0 ? -1 : 1;

					double qi_ = quadPred(d, i);
					if (qi_ < p2_q[i - 1] || qi_ > p2_q[i + 1]) {
						qi_ = linPred(d, i);
					}
					p2_q[i] = qi_;
					p2_n[i] += d;
				}
			}
		}
	}


	/**
	 * Estimates a quantile. If there is no marker for the quantile p, linear
	 * interpolation between the two closest markers is performed. If p is NaN,
	 * NaN will be returned. If there haven't been enough observations or the
	 * markers are not initialized, NaN is returned. If <code>p &lt;= 0.0</code>
	 * or <code>p &gt;= 1.0</code>, the minimum or maximum will be returned.
	 *
	 * @param p
	 *            any number
	 * @return a number that is estimated to be bigger than 100p percent of all
	 *         numbers or Double.NaN, if no data is available
	 */
	public double quantile(double p) {
		if (Double.isNaN(p) || p2_n == null || this.count < p2_n.length)
			return Double.NaN;
		if (p <= 0.0)
			return p2_q[0];
		if (p >= 1.0)
			return p2_q[p2_q.length - 1];
		int idx = Arrays.binarySearch(p2_n_increment, p);
		if (idx < 0) {
			int left = -idx - 2;
			int right = -idx - 1;
			double pl = p2_n_increment[left];
			double pr = p2_n_increment[right];
			return (p2_q[left] * (pr - p) + p2_q[right] * (p - pl)) / (pr - pl);
		}
		return p2_q[idx];
	}

	public double[] markers() {
		if (this.count < p2_q.length) {
			double[] result=new double[count];
			double[] markers=new double[p2_q.length];
			double[] pw_q_copy=p2_q.clone();
			Arrays.sort(pw_q_copy);
			for(int i=pw_q_copy.length-count,j=0;i<pw_q_copy.length;i++,j++){
				result[j]=pw_q_copy[i];
			}
			for(int i=0;i<pw_q_copy.length;i++){
				markers[i]=result[(int)Math.round((count-1)*i*1.0/(pw_q_copy.length-1))];
			}
			return markers;
		}
		return this.p2_q;
	}

	private double quadPred(int d, int i) {
		double qi = p2_q[i];
		double qip1 = p2_q[i + 1];
		double qim1 = p2_q[i - 1];
		int ni = p2_n[i];
		int nip1 = p2_n[i + 1];
		int nim1 = p2_n[i - 1];

		double a = (ni - nim1 + d) * (qip1 - qi) / (nip1 - ni);
		double b = (nip1 - ni - d) * (qi - qim1) / (ni - nim1);
		return qi + (d * (a + b)) / (nip1 - nim1);
	}

	private double linPred(int d, int i) {
		double qi = p2_q[i];
		double qipd = p2_q[i + d];
		int ni = p2_n[i];
		int nipd = p2_n[i + d];

		return qi + d * (qipd - qi) / (nipd - ni);
	}

	public int getCount() {
		return count;
	}

	public double[] getQuartileList() {
		return p2_n_increment;
	}
}
